(ns ^{:doc "A simulator of diabetes patients using standard sim tools."
      :author "Lucy Bridges and James Cunningham"}
  simulator.simulations.diabetes1
  (:use simulator.simulation
        [simulator.utils :only (rand-range)]
        [simulator.simulation utils standard]
        simulator.simulation.coding.read
        simulator.pathways
        simulator.pathways.utils
        simulator.time))

;; Generic patient information leaf nodes

(simple-node alcohol-screening-entry nil :random-patient-registration-event) ;; done
(simple-node new-patient-screening-entry nil :random-patient-registration-event) ;; done
(simple-node smoking-cessation-advice-entry nil :random-patient-registration-event) ;; done
(simple-node bmi-entry nil :random-patient-registration-event) ;; done
(simple-node weight-entry nil :random-patient-registration-event) ;; done
(simple-node height-entry nil :random-patient-registration-event) ;; done
(simple-node systolic-bp-entry nil :random-patient-registration-event) ;; done
(simple-node diastolic-bp-entry nil :random-patient-registration-event) ;; done

;; Diabetes medications leaf nodes

(simple-node insulin-entry nil :diabetes) ;; done
(simple-node metformin-entry nil :diabetes) ;; done
(simple-node sulphonylurea-entry nil :diabetes) ;; done
(simple-node alpha-glucosidase-inhibitor-entry nil :diabetes) ;; done
(simple-node prandial-glucose-regulator-entry nil :diabetes) ;; done
(simple-node glitazone-entry nil :diabetes) ;; done
(simple-node gpl-1-analogue-entry nil :diabetes) ;; done
(simple-node dpp4-inhibitor-entry nil :diabetes) ;; done

;; Diabetes monitoring leaf nodes
 
(simple-node hba1c-entry nil :diabetes) ;;done
(simple-node diabetes-annual-review-entry nil :diabetes) ;;done
(simple-node fasting-blood-glucose-entry nil :diabetes) ;;done
(simple-node education-plan-referral-entry nil :diabetes) ;;done
(simple-node retinal-screening-entry nil :diabetes) ;;done
(simple-node foot-examination-entry nil :diabetes) ;;done
(simple-node neuropathy-testing-entry nil :diabetes) ;;done
(simple-node diabetic-dietary-review-entry nil :diabetes) ;;done
(simple-node foot-risk-classification-entry nil :diabetes) ;;done
(simple-node serum-glucose-entry nil :diabetes) ;;done
(simple-node total-cholesterol-entry nil :diabetes) ;;done
(simple-node microalbuminuria-testing-entry nil :diabetes) ;;done
(simple-node flu-vaccination-entry nil :diabetes)

;; Cardio drugs

(simple-node warfarin-entry nil :diabetes)
(simple-node aspirin-entry nil :diabetes)
(simple-node statins-entry nil :diabetes)
(simple-node ace-entry nil :diabetes)

;; Diabetes risks leaf nodes

(simple-node obesity-entry nil :diabetes) ;; done
(simple-node retinopathy-entry nil :diabetes) ;; done
(simple-node foot-amputation-entry nil :diabetes) ;; done
(simple-node neuropathy-entry nil :diabetes) ;; done
(simple-node proteinuria-entry nil :diabetes) ;; done
(simple-node cvd-entry 
    (children (possibilities
                 warfarin-entry 1/10
                 aspirin-entry 1/5 
                 statins-entry 1/7
                 ace-entry 1/9
                 ) 1 0)
             :diabetes) ;;done
(simple-node hypertension-entry  
    (children (possibilities
                 warfarin-entry 1/10
                 aspirin-entry 1/5 
                 statins-entry 1/7
                 ace-entry 1/9
                 ) 1 0) :diabetes)
(simple-node ckd-entry nil :diabetes) ;;done

;; Generic patient information

(repeating-node random-patient-registration-event #(years 0 20) 
                nil :random-patient-registration-event)

(repeating-node random-admin-event #(years 0 3) nil :random-admin-event)

(repeating-node random-medication-event #(years 0 4) nil :random-medication-event)

(repeating-node random-blood-test #(years 0 3) nil :random-blood-test)

(repeating-node random-measurements #(years 0 8) 
                nil :random-measurements)


;; Diabetes specific information

(repeating-node diabetes-type-1-medications #(months 1 3) 
  (children (possibilities
              insulin-entry 1
              metformin-entry 1/200)              
            1 0) 
  :diabetes-type-1)


(repeating-node diabetes-type-2-medications #(months 2 6) 
  (children (possibilities 
              insulin-entry 1/100
              metformin-entry 2/10
              sulphonylurea-entry 1/10
              alpha-glucosidase-inhibitor-entry 1/50
              prandial-glucose-regulator-entry 1/30
              glitazone-entry 1/30
              gpl-1-analogue-entry 1/40
              dpp4-inhibitor-entry 1/40)
            1 0) 
  :diabetes-type-2)


(repeating-node diabetes-type-1-event #(years 0 10)
     (children diabetes-type-1-medications
            1 0) 
    :diabetes-type-1)

(repeating-node diabetes-type-2-event #(years 0 10)
     (children diabetes-type-2-medications
            1 0) 
    :diabetes-type-2)

(repeating-node diabetes-monitoring-event #(months 1 12)
  (children (possibilities
              bmi-entry 2/10
              hba1c-entry 9/10
              diabetes-annual-review-entry 1/10
              fasting-blood-glucose-entry 7/10
              serum-glucose-entry 7/10
              total-cholesterol-entry 7/10
              education-plan-referral-entry 1/18
              retinal-screening-entry 3/10
              foot-examination-entry 2/10          
              microalbuminuria-testing-entry 3/10
              flu-vaccination-entry 1/10               
              neuropathy-testing-entry 1/10             
              diabetic-dietary-review-entry 1/10                
              foot-risk-classification-entry 1/18)
            1 0) 
  :diabetes)

(repeating-node diabetes-risks #(years 20 60)
  (children (possibilities
              obesity-entry 1/2
              retinopathy-entry 1/10
              foot-amputation-entry 1/100
              neuropathy-entry 1/40
              proteinuria-entry 1/9
              cvd-entry 1/20
              hypertension-entry 1/15
              ckd-entry 1/50
              )
            1 0)
  :diabetes)


(lifeline-fn birth                    0
             random-patient-registration-event #(years 5 10)
             random-admin-event         #(years 5 10)
             random-medication-event    #(years 5 10)
             random-measurements        #(years 10 15)
             random-blood-test          #(years 5 10)             
             diabetes-type-1-event      #(years-normal 10 10)
             ;;diabetes-type-2-event      #(years-normal 65 25)
             diabetes-monitoring-event  #(years 0 20)
             diabetes-risks             #(years 40 80)
             death                      #(years-normal 64 10)) ;;change to 70 for DM2


(patient-fn #(random-patient [1965 20] ethnicity-all ["gp"]))

(journal-formatter default-journal-formatter)

(patient-formatter default-patient-formatter)

(journal-map
  {
   :random-admin-event general-admin
   :random-medication-event general-medications
   :random-measurements general-measurements
   :random-blood-test general-blood-tests
   :random-patient-registration-event new-patient-details
   :bmi-entry (measurement-entry bmi #(nd 23 5) "kg/m^2")
   :alcohol-screening-entry (measurement-entry alcohol-screening #(nd 2 1/12) "/12")
   :smoking-cessation-advice-entry (entry smoking-cessation-advice)
   :weight-entry (measurement-entry weight #(nd 90 40/150) "kg")
   :height-entry (measurement-entry height #(nd 185 120/250) "cm")
   :systolic-bp-entry (measurement-entry systolic-bp #(rand-range 80 130) "mmHg")
   :diastolic-bp-entry (measurement-entry diastolic-bp #(rand-range 40 85) "mmHg")
   :diabetes-type-1-event (entry diabetes-type-1)
   :diabetes-type-2-event (entry diabetes-type-2)
   :hba1c-entry (measurement-entry hba1c-ifcc #(nd 60 20) "mmol/mol")
   :insulin-entry (entry insulin)
   :metformin-entry (entry metformin)
   :sulphonylurea-entry (entry sulphonylureas)
   :alpha-glucosidase-inhibitor-entry (entry alpha-glucosidase-inhibitors)
   :prandial-glucose-regulator-entry (entry prandial-glucose-regulators)
   :glitazone-entry (entry glitazones)
   :gpl-1-analogue-entry (entry gpl-1-analogues)
   :dpp-4-inhibitor-entry (entry dpp-4-inhibitors)
   :fasting-blood-glucose-entry (measurement-entry fasting-blood-glucose-level #(rand-range 0.9 10) "mmol/L")
   :serum-glucose-entry (measurement-entry serum-glucose #(rand-range 1.4 9) "mmol/L")
   :serum-random-glucose-entry (measurement-entry serum-random-glucose #(rand-range 1.4 9) "mmol/L")
   :total-cholesterol-entry (measurement-entry total-cholesterol #(nd 3 2) "")
   :education-plan-referral-entry (entry education-plan-referral)
   :retinal-screening-entry (entry retinal-screening)
   :foot-examination-entry (entry foot-examination)
   :microalbuminuria-testing-entry (entry microalbuminuria-testing)
   :flu-vaccination-entry (entry flu-vaccination)
   :neuropathy-testing-entry (entry neuropathy-testing)
   :diabetic-dietary-review-entry (entry diabetic-dietary-review)
   :foot-risk-classification-entry (entry foot-risk-classification)
   :obesity-entry (measurement-entry obesity #(rand-range 30 50) "kg/m^2")
   :retinopathy-entry (entry retinopathy)
   :foot-amputation-entry (entry foot-amputation)
   :neuropathy-entry (entry neuropathy)
   :proteinuria-entry (entry proteinuria)
   :cvd-entry (entry cardiovascular-disease)
   :hypertension-entry (entry hypertension)
   :ckd-entry (entry chronic-kidney-disease)
   :ace-entry (entry (concat ramipril-ramipril-with-felodipine captopril cilazapril enalapril-maleate fonsinopril-sodium))
   :warfarin-entry (entry warfarin)
   :statins-entry (entry simvastatin)
   :aspirin-entry (entry aspirin)
   :born nil
   :died nil
   })

