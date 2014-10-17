(ns ^{:doc "Tools for running a standard simulation."
      :author "James Cunningham"}
  simulator.simulation.standard
  (:use [simulator simulation time]
        incanter.distributions)
  (:import [java.util Date Calendar]
           java.text.SimpleDateFormat))

(defn random-patient
  "Generates a random patient selecting dob from a normal distribution.
  id-fn defaults to java's randomUUID."
  ([id-fn [birth-mean years-sd] ethnicity-bag gp-bag]
      (new-patient (id-fn)
               (rand-nth [:male :female])
               (random-date-normal birth-mean years-sd)
               nil
               (rand-nth ethnicity-bag)
               (rand-nth gp-bag)))
  ([[birth-mean years-sd] ethnicity-bag gp-bag]
     (random-patient (fn [] (.toString (java.util.UUID/randomUUID)))
                     [birth-mean years-sd] ethnicity-bag gp-bag)))

;; smoking statuses
(def quit-smoking 2)
(def current-smoker 1)
(def non-smoker 0)

;; note - the recording of smoking status presumes that the record is
;; being produced in the present
(defn get-patient-smoking-status
  "Generates a random smoking status for a given patient.
  There's a 10% chance that a patient will smoke at some point.
  There's a 50% chance a smoker will stop at some point.
  For smokers a start and stop age are determined. 
  start age = normal distribution 18 years mean 4 years sd
  stop age = normal distribution 40 years mean 10 years sd.
  returns a smoking status (see defs above)."
  [patient]
  (let [dob (:dob patient)
        start-age (max (years-to-ms 8)
                       (long (draw (normal-distribution
                                    (years-to-ms 18)
                                    (years-to-ms 4)))))
        start-date (add-time-to-date dob start-age)
        stop-age (long (draw (normal-distribution
                              (years-to-ms 40)
                              (years-to-ms 10))))
        stop-date (add-time-to-date dob stop-age)
        started (if (> 0.1 (rand)) start-age)
        quit (if (and start-age (> 0.5 (rand))) stop-age)]
    (if (and start-date (not (after-now? start-date)))
      (if (and stop-date (not (after-now? stop-date)))
        quit-smoking current-smoker) non-smoker)))

(defn default-patient-formatter
  "Formats patient info for output in form:
  id,sim-name,dob (year), smoking status, sex"
  [patient sim-name]
  (reduce str (interpose "," (list (:id patient)
                                   sim-name
                                   (get-year (:dob patient))
                                   (get-patient-smoking-status patient)
                                   (name (:sex patient))))))

(defn default-journal-formatter
  "Formats raw journal info for output in form:
  id, sim-name, date, ethnicity, null, null
  followed by the following line for each journal entry:
  id, sim-name, date (MMM dd yyy hh:mma), entry"
  [patient journal sim-name]
  (letfn [(fmt-date [date]
            (.format (SimpleDateFormat. "MMM dd yyyy hh:mma") date))
          (fmt-entry [e]
            (str (:id e) "," sim-name "," (fmt-date (:date e)) "," (:event e)))
          (fmt-eth []
            (str (:id patient) "," sim-name "," (fmt-date (:dob patient)) ","
                 (:ethnicity patient) ",null,null\n"))]
    (str (fmt-eth) (apply str (interpose "\n" (pmap fmt-entry journal))))))