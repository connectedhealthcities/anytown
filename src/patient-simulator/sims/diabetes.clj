(ns patient-simulator.simulations.diabetes
  (:use [patient-simulator pathways time simulation patient utils]
        [patient-simulator.coding read]))

(set-sim-name "diabetes-sim")

;; nodes

(def birth (make-simple-node :born))

(def-simple-node diabetes-type-i nil :diabetes-i)

(def-simple-node diabetes-type-ii nil :diabetes-ii)

(def-simple-node get-diabetes
  (list (child diabetes-type-i 1 #(years 10 15))
        (child (list (possibility diabetes-type-i 1/1)
                     (possibility diabetes-type-ii 1/12) 1 #(years 1 2)))
        (child diabetes-type-ii 1 #(years 20 40)))
  :diabetes)

(def random-medical-event
  (make-repeating-node #(years 0 10) :random nil))

(def death (make-death-node :died))

;; simulation gubbins

(defn patient-fn
  [] (assoc (random-patient [1965 20] ethnicity-all ["gp-codes-go-here"]) :sex :male)

(defn lifeline-fn
  [] (initial-lifeline [[birth 0]
                        [random-medical-event #(years 0 10)]
                        [get-diabetes #(years 0 10)]
                        [death #(years-normal 80 10)]]))

(defn- append-null-null [s] (str s ",null,null"))

(def journal-map
  {
   :born             ["xyz001,0,1ml" "xyz002"] 
   :random           (map append-null-null all-assorted)
   :get-diabetes     nil
   :diabetes-type-i  nil
   :diabetes-type-ii nil
   :died             nil
   })
