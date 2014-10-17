(ns ^{:doc "A simulator of normal patients (having no chronic illnesses) using standard sim tools."
      :author "Lucy Bridges"}
  simulator.simulations.normal
  (:use simulator.simulation
        [simulator.simulation utils standard]
        simulator.simulation.coding.read
        simulator.pathways
        simulator.pathways.utils
        simulator.time))
  )


(simple-node alcohol-screening-entry nil :new-patient-registration) ;; done
(simple-node new-patient-screening-entry nil :new-patient-registration) ;; done
(simple-node smoking-cessation-advice-entry nil :new-patient-registration) ;; done
(simple-node bmi-entry nil :new-patient-registration) ;; done
(simple-node weight-entry nil :new-patient-registration) ;; done
(simple-node height-entry nil :new-patient-registration) ;; done
(simple-node systolic-bp-entry nil :new-patient-registration) ;; done
(simple-node diastolic-bp-entry nil :new-patient-registration) ;; done

(simple-node new-patient-registration 
    (children (possibilities 
              systolic-bp-entry 8/10              
              diastolic-bp-entry 8/10
              height-entry 7/10
              weight-entry 7/10
              bmi-entry 9/10
              new-patient-screening-entry 9/10
              alcohol-screening-entry 6/10
              smoking-cessation-advice-entry 3/10)
            1 0) 
    :new-patient-registration)

(repeating-node random-admin-event #(years 0 4) nil :random-admin-event)

(repeating-node random-medication-event #(years 0 5) nil :random-medication-event)

(repeating-node random-measurements #(years 0 8) nil :random-measurements)

(repeating-node random-blood-test #(years 0 10) nil :random-blood-test)

(lifeline-fn birth 0
             new-patient-registration #(years 15 35)
             random-admin-event       #(years 5 10)
             random-medication-event  #(years 5 10)
             random-measurements      #(years 5 10)
             random-blood-test        #(years 5 10)  
             death                    #(years-normal 80 10))

(patient-fn #(random-patient [1965 20] ethnicity-all ["gp"]))

(journal-formatter default-journal-formatter)

(patient-formatter default-patient-formatter)

(journal-map
 (merge {
   :random-admin-event general-admin
   :random-medication-event general-medications
   :random-blood-test general-blood-tests
   :random-measurements general-measurements
   :bmi-entry (measurement-entry bmi #(nd 23 5) "kg/m^2")
   :alcohol-screening-entry (measurement-entry alcohol-screening #(nd 2 1/12) "/12")
   :new-patient-screening-entry (entry new-patient-screening)
   :smoking-cessation-advice-entry (entry smoking-cessation-advice)
   :weight-entry (measurement-entry weight #(nd 90 40/150) "kg")
   :height-entry (measurement-entry height #(nd 185 120/250) "cm")
   :systolic-bp-entry (measurement-entry systolic-bp #(rand-range 80 130) "mmHg")
   :diastolic-bp-entry (measurement-entry diastolic-bp #(rand-range 40 85) "mmHg")
   :born nil
   :died nil
   }))

