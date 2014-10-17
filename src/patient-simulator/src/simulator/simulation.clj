(ns ^{:doc "Patient simulation library."
      :author "James Cunningham"}
  simulator.simulation  
  (:use [simulator time simulation pathways utils]        
        simulator.pathways.utils
        clojure.java.io)
  (:import java.util.Date))

;; change from record?
(defrecord Patient [id sex dob death ethnicity gp-code])

(defn new-patient
  "Constructs a new Patient object.
  (Convenience to avoid having to :import Patient class)."
  [id sex dob death ethnicity gp-code]
  (Patient. id sex dob death ethnicity gp-code))

(defn select-event
  "Selects an event entry from a journal map where event is a key. 
  The event is picked either randomly from the list or, if the even
  has been constructed using the weighted function, as a weighted
  choice. If the selected event is a function it is evaluated."
  [event journal-map]
  (let [entry (event journal-map)
        selection (if (coll? entry)
                    (let [choice (weighted-choice weight entry)]
                      (if (:weight choice) (:choice choice) choice)) ;; via 'weighted'
                    entry)]
    (if (fn? selection) (selection) selection)))

(defn journal-entry
  "A journal entry (pre any processing)."
  [id sim-id event date]
  {:id id
   :sim-id sim-id
   :event event
   :date date})

(defn create-date-limiting-stop-fn
  "Given a patient and a date (optional, defaults to now), creates
  a function for use in run-lifeline which will halt the run once
  the effective time of events is later than the given date."
  ([patient date]
     (let [acc (atom 0)
           cutoff (- (date-in-ms date) (date-in-ms (:dob patient)))]
       (fn [lifeline]
         ;; we presume that lifeline's already been checked with (empty? ..)
         (> (swap! acc + (second (peek (:future lifeline)))) cutoff))))
  ([patient] (create-date-limiting-stop-fn patient (Date.))))

(defn produce-journal
  "Given a patient a (run) lifeline, a journal-map and a sim id,
  generates a list of journal entries with the event drawn from
  the journal-map (using select-event). Takes a stopper function
  to pass as stop? parameter in run-lifeline - defaults to 
  'halt at present date' function produced by create-date-limiting-stop-fn
  above."
  ([patient lifeline journal-map sim-id stopper]
     (let [life (past (run-lifeline lifeline stopper))
           dates (rest (reductions add-time-to-date
                                   (:dob patient) (map :time life)))]
       (filter (comp not nil? #(:event %)) ;; remove nil events
               (pmap (fn [fact date] (journal-entry
                                      (:id patient)
                                      sim-id
                                      (select-event (:event fact) journal-map)
                                      date))
                     life dates))))
  ([patient lifeline journal-map sim-id]
     (produce-journal patient lifeline journal-map sim-id
                      (create-date-limiting-stop-fn patient))))

(defn simulate-patients
  "Simulates patients and writes results out to files.
  number - number of patients to simulate
  create-fn - function to create a random patient (no args)
  lifeline-fn - function to create the initial lifeline of a patient (no args)
  journal-map - map used to conver event key to string
  journal-formatter - function to convert raw journal to string
                      (arg: patient journal sim-name)
  patient-formatter - function to convert patient to string 
                      (arg: patient sim-name)
  sim-name - placed in raw journal representation
  journal-file - file to write journal to
  patient-file - file to write demographics to"
  [number create-fn lifeline-fn journal-map
   journal-formatter patient-formatter
   sim-name journal-file patient-file]
  (with-open [journals (writer journal-file :append true)
              records (writer patient-file :append true)]
    (let [counter (atom 0)]
      (while (< @counter number)
        (let [patient (create-fn)
              lifeline (lifeline-fn)
              raw-journ (produce-journal patient lifeline journal-map sim-name)
              journal (journal-formatter patient raw-journ sim-name)
              record (patient-formatter patient sim-name)]
          (if (not (or (nil? raw-journ) (empty? raw-journ)))
            (do (.write journals (str journal "\n"))
                (.write records (str record "\n"))
                (swap! counter inc))))))))