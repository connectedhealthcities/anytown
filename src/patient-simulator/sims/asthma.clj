(ns ^{:doc "A simulator of asthma patients using standard sim tools."
      :author "James Cunningham"}
  simulator.simulations.asthma
  (:use simulator.simulation
        [simulator.simulation utils standard]
        simulator.simulation.coding.read
        simulator.pathways
        simulator.pathways.utils
        simulator.time))

(limited-repeating-node terbutaline #(weeks 4 52) (weeks-to-ms 52) nil :asthma)

(limited-repeating-node perbuterol #(weeks 4 52) (weeks-to-ms 52) nil :asthma)

(limited-repeating-node salbutamol #(weeks 4 52) (weeks-to-ms 52) nil :asthma)

(scrub-tags-node asthma-resolved-node [:asthma] nil :asthma-resolved)

(simple-node stop-prescription
             (children (possibilities asthma-resolved-node 1/10) 1 0))

(simple-node choose-prescription-stage-1
             (children terbutaline 1 0
                       perbuterol  1 0
                       salbutamol  8 0)
             :asthma)

(simple-node prescription-stage-1
             (children stop-prescription           1 0
                       choose-prescription-stage-1 9 0)
             :ashtma)

(simple-node prescription-stage-3 nil :asthma)

(simple-node prescription-stage-2
             (children (possibilities prescription-stage-3 2/10) 1 0)
             :asthma)

(simple-node prednisolone-node nil :asthma)

(simple-node management-plan-node nil :asthma)

(simple-node flu-vaccination-entry nil :asthma)

(simple-node annual-review
             (children (possibilities management-plan-node 3/10) 1 0)
             :asthma)

(simple-node fvc nil :asthma)

(simple-node fev1 nil :asthma)

(simple-node fev1-fec-percent nil :asthma)

(simple-node spirometry-screening-node
             (children (all-of fvc fev1 fev1-fec-percent) 1 0)
             :asthma)

(simple-node pefr nil :asthma)

(simple-node tests (children spirometry-screening-node 2 0
                             pefr                      8 0)
             :asthma)

(repeating-node asthma-management-node #(years 1 5)
                (children (possibilities prescription-stage-1 9/10
                                         prescription-stage-2 2/10
                                         tests                4/10
                                         annual-review        8/10
                                         flu-vaccination-entry 3/10
                                         prednisolone-node    1/20) 1 0)
                :asthma)

(simple-node asthma-diagnosis-node
             (children asthma-management-node 1 #(years 1 5)))

(repeating-node random-admin-event #(years 0 4) 
                nil :random-admin-event)

(repeating-node random-medication-event #(years 0 5) 
                nil :random-medication-event)

(repeating-node random-blood-test #(years 0 8) 
                nil :random-blood-test)

(repeating-node random-measurements #(years 0 8) 
                nil :random-measurements)

(repeating-node random-patient-registration-event #(years 0 20) 
                nil :random-patient-registration-event)


(lifeline-fn birth                    0
             random-patient-registration-event #(years 5 10)
             random-admin-event       #(years 5 10)
             random-medication-event  #(years 5 10)
             random-measurements      #(years 10 15)
             random-blood-test        #(years 5 10)             
             asthma-diagnosis-node    #(years-normal 15 12)
             death                    #(years-normal 80 10))

(patient-fn #(assoc (random-patient [1965 20] ethnicity-all ["gp"])
               :sex :male))

(journal-formatter default-journal-formatter)

(patient-formatter default-patient-formatter)

(def male-journal-map
  {:pefr (map #(fn [] (str % "," (rand-range 400 700) ",L/min"))
              ["339c." "339d." "339A." "339B."])
   :fvc (map #(fn [] (str % "," (rand-range 3 6.5) ",L"))
             ["3396." "339h." "339s."])
   :fev1-fec-percent (fn [] (str "339R.," (rand-range 40 150) ",%"))
   :fev1 (fn [] (str "339O.," (rand-range 2 5) ",L"))})

(def female-journal-map
  {:pefr (map #(fn [] (str % "," (rand-range 300 480) ",L/min"))
              ["339c." "339d." "339A." "339B."])
   :fvc (map #(fn [] (str % "," (rand-range 2.5 5) ",L"))
             ["3396." "339h." "339s."])
   :fev1-fec-percent (fn [] (str "339R.," (rand-range 40 150) ",%"))
   :fev1 (fn [] (str "339O.," (rand-range 1.5 4) ",L"))})

;; at the moment only does males
(journal-map 
 (merge {:born nil
         :asthma-diagnosis-node
         (cons (weighted nil 5)
               (map #(weighted % 95/7)
                    (map append-null-null asthma-diagnosis)))
         :management-plan-node (map append-null-null asthma-management-plan)
         :terbutaline (map append-null-null terbutaline-prescription)
         :perbuterol (map append-null-null perbuterol-prescription)
         :salbutamol (map append-null-null salbutamol-prescription)
         :stop-prescription nil
         :asthma-resolved-node (entry asthma-resolved)
         :prescription-stage-1 nil
         :prescription-stage-2 (entry corticosteroids)
         :prescription-stage-3 (entry long-acting-beta-2)
         :prednisolone-node (entry prednisolone)
         :annual-review (map append-null-null asthma-annual-review)   
         :flu-vaccination-entry (entry flu-vaccination) 
         :tests nil
         :died nil
         :spirometry-screening-node (entry spirometry-screening)
         :random-admin-event general-admin
         :random-medication-event general-medications
         :random-blood-test general-blood-tests
         :random-measurements general-measurements
         :random-patient-registration-event new-patient-details
         }
        male-journal-map))
