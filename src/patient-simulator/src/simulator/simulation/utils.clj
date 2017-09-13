(ns ^{:doc "Tools for running a standard simulation."
      :author "James Cunningham"}
  simulator.simulation.utils
  (:use simulator.pathways
        simulator.pathways.utils))

(def birth (make-simple-node :born))

(def death (make-death-node :died))

(defn entry
  "Transforms a collection of codes into a collection of
  strings of the form:
  code,null,null."
  [codes] (map #(str % ",NULL,NULL") codes))

(defn measurement-entry
  "Transforms a collection of codes into a collection of
  functions. Each function returns a string of the form:
  code,range,units
  where range is the result of calling range-fn.
  For use in journal-map."
  [codes range-fn units]
  (map #(fn [] (str % "," (range-fn) "," units)) codes))

(defn append-null-null
  [s] (str s ",null,null"))

(defmacro def-ns-def
  "I heard you like macros...
  Shortcut for defining macros which def a variable of the 
  form *name*. Used to create:
  - journal-map
  - patient-fn
  - journal-formatter
  - patient-formatter
  which are used to run a simulation via core."
  [name]
  (let [var (str "*" name "*")]
    `(defmacro ~name [f#]
       "...so we put a macro in your macro"
       `(def ~(with-meta (symbol (str *ns* "/" ~var))
                {:dynamic true}) ~f#))))

(def-ns-def journal-map)

(def-ns-def patient-fn)

(def-ns-def journal-formatter)

(def-ns-def patient-formatter)

(defmacro lifeline-fn [& event-times]
  `(def ~(with-meta (symbol (str *ns* "/" "*lifeline-fn*"))
           {:dynamic true})
     (fn [] (create-lifeline ~@event-times))))


;; stats utils (need to organise properly)

;;(defn nd [m sd] (draw (normal-distribution m sd)))
