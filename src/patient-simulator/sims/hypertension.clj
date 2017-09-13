(ns ^{:doc "A simulator of hypertension patients using standard sim tools."
      :author "James Cunningham"}
  simulator.simulations.hypertension
  (:use simulator.simulation
        [simulator.utils :only (rand-range)]
        [simulator.simulation utils standard]
        simulator.simulation.coding.read
        simulator.pathways
        simulator.pathways.utils
        simulator.time))


;; Generic patient information

(repeating-node random-patient-registration-event #(years 0 20) 
                nil :random-patient-registration-event)

(repeating-node random-admin-event #(years 0 6) nil :random-admin-event)

(repeating-node random-medication-event #(years 0 4) nil :random-medication-event)

(repeating-node random-blood-test #(years 0 5) nil :random-blood-test)

(repeating-node random-measurements #(years 0 8) 
                nil :random-measurements)

;; Blood tests

(simple-node serum-ldl-cholesterol-measurement nil :hypertension) ;; done

(simple-node serum-hdl-cholesterol-measurement nil :hypertension) ;; done

(simple-node serum-cholesterol-and-level-measurement nil :hypertension) ;; done

(simple-node serum-cholesterol-normal-measurement nil :hypertension) ;; done

(simple-node serum-cholesterol-raised-measurement nil :hypertension) ;; done

(simple-node serum-cholesterol-very-high-measurement nil :hypertension) ;; done

(simple-node fasting-blood-glucose-level-measurement nil :hypertension) ;; done

(simple-node blood-glucose-measurement-serum-measurement nil :hypertension) ;; done

(simple-node random-blood-glucose-level-measurement nil :hypertension) ;; done

(simple-node urine-protein-measurement nil :hypertension) ;; done

(simple-node urine-albumin-level-measurement nil :hypertension) ;; done

(simple-node blood-urea-renal-function-measurement nil :hypertension) ;; done

(simple-node creatinine-serum-measurement nil :hypertension) ;; done

(simple-node potassium-serum-measurement nil :hypertension) ;; done

(simple-node sodium-serum-measurement nil :hypertension) ;; done

(simple-node urea-serum-measurement nil :hypertension) ;; done

(simple-node haemoglobin-measurement nil :hypertension) ;; done

(simple-node mcv-measurement nil :hypertension) ;; done

(simple-node rbc-measurement nil :hypertension) ;; done

(simple-node wbc-measurement nil :hypertension) ;; done

(simple-node plt-measurement nil :hypertension) ;; done

(simple-node albumin-serum-measurement nil :hypertension) ;; done

(simple-node alkaline-phosphatase-measurement nil :hypertension) ;; done

(simple-node total-bilirubin-measurement nil :hypertension) ;; done

(simple-node total-protein-measurement nil :hypertension) ;; done

(simple-node oe-weight nil :hypertension) ;; done

(simple-node body-mass-index nil :hypertension) ;; done

(simple-node corrected-calcium-entry nil :hypertension) ;; done

(simple-node phosphate-serum-entry nil :hypertension) ;; done

(simple-node alt-entry nil :hypertension) ;; done

(simple-node estimated-glomerular-filtration-rate-entry nil :hypertension) ;; done

(simple-node thyroid-function-test-entry nil :hypertension) ;; done

(simple-node serum-tsh-level-entry nil :hypertension) ;; done

(simple-node blood-tests ;; done
  (children 
        serum-ldl-cholesterol-measurement 1 0
        serum-hdl-cholesterol-measurement 1 0
        serum-cholesterol-and-level-measurement 1 0
        serum-cholesterol-normal-measurement 1 0
        serum-cholesterol-raised-measurement 1 0
        serum-cholesterol-very-high-measurement 1 0
        fasting-blood-glucose-level-measurement 1 0
        blood-glucose-measurement-serum-measurement 1 0
        random-blood-glucose-level-measurement 1 0
        urine-protein-measurement 1 0
        urine-albumin-level-measurement 1 0
        blood-urea-renal-function-measurement 1 0
        creatinine-serum-measurement 1 0
        potassium-serum-measurement 1 0
        sodium-serum-measurement 1 0
        urea-serum-measurement 1 0
        haemoglobin-measurement 1 0
        mcv-measurement 1 0
        rbc-measurement 1 0
        wbc-measurement 1 0
        plt-measurement 1 0
        albumin-serum-measurement 1 0
        alkaline-phosphatase-measurement 1 0
        total-bilirubin-measurement 1 0
        total-protein-measurement 1 0
        oe-weight 1 0
        body-mass-index 1 0
        corrected-calcium-entry 1 0
        phosphate-serum-entry 1 0
        alt-entry 1 0
        estimated-glomerular-filtration-rate-entry 1 0
        thyroid-function-test-entry 1 0
        serum-tsh-level-entry 1 0))

;; Diagnosis

(simple-node cardiovascular-disease-diagnosis nil :hypertension) ;; done

(simple-node bp-sys-elevated nil :hypertension)

(simple-node bp-dia-elevated nil :hypertension)

(simple-node bp-elevated-result
  (children
   (all-of bp-sys-elevated bp-dia-elevated) 1 0)
  :hypertension) ;; done

(simple-node bp-sys-normal nil :hypertension)

(simple-node bp-dia-normal nil :hypertension)

(simple-node bp-normal-result
             (children (all-of bp-sys-elevated bp-dia-elevated) 1 0)
  :hypertension) ;; done

(scrub-tags-node hypertension-resolved-node [:hypertension] nil :hypertension-resolved-node) 

(simple-node bp-normal-result-with-resolved ;; done
  (children (possibilities
                bp-normal-result 1
                hypertension-resolved-node 1/10) 1 0)
  :hypertension)

(simple-node blood-pressure-reading-stage-2 ;; done
  (children
      bp-elevated-result 65 0
      bp-normal-result 35 0)
  :hypertension)

(simple-node blood-pressure-reading-stage-1 ;; done
  (children 
      bp-elevated-result 90 0
      bp-normal-result-with-resolved 10 0)
  :hypertension)

(simple-node blood-pressure-reading-checkup ;; done
  nil
  :hypertension)


(simple-node asprin-warfarin nil :hypertension) ;; done

(simple-node other-statins ;; done
  nil
  :hypertension)

(simple-node simvastatin-node ;; done
  nil
  :hypertension)

(simple-node statins-node ;; done
  (children
      other-statins 15 0
      simvastatin-node 85 0)
  :hypertension)

(simple-node general-prescriptions ;; done
  (children 
      asprin-warfarin 1 0
      statins-node 1 0)
  :hypertension)

(simple-node ace-V ;; done
  nil
  :hypertension)

(simple-node ace-W ;; done
  nil
  :hypertension)

(simple-node ace-X ;; done
  nil
  :hypertension)

(simple-node prescribe-ace-inhibitors
  (children 
    ace-V 3 1
    ace-W 1 1
    ace-X 1 1)
  :hypertension)

(simple-node calcium-channel-blockers ;; done
  nil
  :hypertension)

(simple-node central-alpha-antogonists ;; done
  nil
  :hypertension)

(simple-node thiazides ;; done
  (children (possibilities central-alpha-antogonists 6/10) 1 0)
  :hypertension)

(simple-node angiotensin-ii-receptor-antagnoist ;; done
  nil
  :hypertension

  (simple-node beta-blockers ;; done
               nil
               :hypertension))

(simple-node stage-2-presecriptions ;; done
             (children 
              calcium-channel-blockers 1 0
              thiazides 1 0
              angiotensin-ii-receptor-antagnoist 1 0
              beta-blockers 1 0
              prescribe-ace-inhibitors 1 0)
             :hypertension)

(repeating-node hypertension-management-2 #(weeks 45 60) 
                (children (possibilities 
                           blood-pressure-reading-stage-2 98/100
                           blood-tests 8/10
                           stage-2-presecriptions 99/100)
                          1 0)
                :hypertension)

(scrub-tags-node hypertension-management-upgrade [:hypertension-1] 
   (children hypertension-management-2 1 0
            :hypertension)
   :hypertension-management-upgrade)

(repeating-node hypertension-management-1 #(weeks 45 60) 
   (children (possibilities
                 blood-pressure-reading-stage-1 95/100
                 prescribe-ace-inhibitors 99/100
                 general-prescriptions 3/10
                 hypertension-management-upgrade 2/100)
                1 0)
   :hypertension
   :hypertension-1)

(simple-node hypertension-diagnosis-1 ;; done
  (children hypertension-management-1 1 0)
  :hypertension)

(simple-node hypertension-diagnosis-2 ;; done
  (children hypertension-management-2 1 0)
  :hypertension)


;;(defn lifeline-fn-1
 ;; [] (initial-lifeline [[birth 0]
   ;;                     [random-patient-registration-event #(years 5 10)]
     ;;                   [random-admin-event #(years 5 10)]
       ;;                 [random-medication-event #(years 5 10)]
         ;;;               [random-blood-test #(years 5 10)]
            ;;            [hypertension-diagnosis-1 #(years 40 55)]
              ;;          [death #(years-normal 80 10)]]))

;;(defn lifeline-fn-2
 ;; [] (initial-lifeline [[birth 0]
   ;;                     [random-patient-registration-event #(years 5 10)]
     ;;                   [random-admin-event #(years 5 10)]
       ;;                 [random-medication-event #(years 5 10)]
         ;;               [random-blood-test #(years 5 10)]
           ;;             [hypertension-diagnosis-2 #(years 55 80)]
             ;;           [death #(years-normal 80 10)]]))


(lifeline-fn birth                    0
             random-patient-registration-event #(years 5 10)
             random-admin-event       #(years 5 10)
             random-medication-event  #(years 5 10)
             random-measurements      #(years 10 15)
             random-blood-test        #(years 5 10)             
             hypertension-diagnosis-2 #(years 55 80)
             ;;hypertension-diagnosis-1 #(years 40 55)
             death                    #(years-normal 80 10))

(patient-fn #(random-patient [1965 20] ethnicity-all ["gp"]))

(journal-formatter default-journal-formatter)

(patient-formatter default-patient-formatter)


(journal-map
  {
   :random-admin-event general-admin
   :random-medication-event general-medications
   :random-blood-test general-blood-tests
   :random-patient-registration-event new-patient-details
   :born nil
   :serum-ldl-cholesterol-measurement (entry serum-ldl-cholesterol)
   :serum-hdl-cholesterol-measurement (entry serum-hdl-cholesterol)
   :serum-cholesterol-and-level-measurement (measurement-entry serum-cholesterol-and-level #(bd 6.00 5/6) "mmol/L")
   :serum-cholesterol-normal-measurement (entry serum-cholesterol-normal)
   :serum-cholesterol-raised-measurement (entry serum-cholesterol-raised)
   :serum-cholesterol-very-high-measurement (entry serum-cholesterol-very-high)
   :fasting-blood-glucose-level-measurement (measurement-entry fasting-blood-glucose-level
                                                   #(rand-range 0.9 10) "mmol/L")
   :blood-glucose-measurement-serum-measurement (measurement-entry serum-glucose
                                                       #(rand-range 1.4 9) "mmol/L")
   :random-blood-glucose-level-measurement (measurement-entry random-blood-glucose-level
                                                  #(rand-range 1.0 13.0) "mmol/L")
   :urine-protein-test-measurement (measurement-entry urine-protein-test
                                          #(rand-range 0.01 7) "g/L")
   :urine-albumin-level-measurement (measurement-entry urine-albumin-level
                                           #(rand-range 0.1 99) "mg/L")
   :blood-urea-renal-function-measurement (measurement-entry blood-urea-renal-function
                                          #(rand-range 0.3 14) "mmol/L")
   :creatinine-serum-measurement (measurement-entry creatinine-serum
                                        #(rand-range 0.1 16) "umol/L")
   :potassium-serum-measurement (measurement-entry potassium-serum
                                       #(rand-range 0.1 12) "mmol/L")
   :sodium-serum-measurement (measurement-entry sodium-serum
                                    #(rand-range 0.2 200) "mmol/L")
   :urea-serum-measurement (measurement-entry urea-serum
                                  #(rand-range 0.2 12) "mmol/L")
   :haemoglobin-measurement (measurement-entry haemoglobin
                                   #(rand-range 0.1 16) "g/dL")
   :mcv-measurement (measurement-entry mcv
                           #(rand-range 0.2 175) "f/L")
   :rbc-measurement (measurement-entry rbc
                           #(rand-range 0.1 12) "x10^12/l")
   :wbc-measurement (measurement-entry wbc
                           #(rand-range 0.01 12) "x10^9/l")
   :plt-measurement (measurement-entry plt
                           #(rand-range 0.01 30) "x10^9/l")
   :albumin-serum-measurement (measurement-entry albumin-serum
                                     #(rand-range 0.1 60) "g/L") 
   :alkaline-phosphatase-measurement (measurement-entry alkaline-phosphatase
                                            #(rand-range 0.4 120) "U/L")
   :total-bilirubin-measurement (measurement-entry total-bilirubin
                                       #(rand-range 1 24) "umol/L")
   :total-protein-measurement (measurement-entry total-protein
                                     #(rand-range 0.05 9) "g/L")
   :oe-weight (measurement-entry weight
                                 #(rand-range 0.1 150) "Kg")
   :body-mass-index (measurement-entry bmi
                                       #(nd 23 5) "kg/m^2")
   :corrected-calcium-entry (measurement-entry corrected-calcium
                                         #(rand-range 0.5 5) "mmol/L")
   :phosphate-serum-entry (measurement-entry phosphate-serum
                                       #(rand-range 0.05 2) "mmol/L")
   :alt-entry (measurement-entry alt
                           #(rand-range 1 60) "U/L")
   :estimated-glomerular-filtration-rate-entry (measurement-entry estimated-glomerular-filtration-rate
                                                            #(rand-range 1 140) "mL/min")
   :thyroid-function-test-entry (measurement-entry thyroid-function-test
                                             #(rand-range 0.8 102) "")
   :serum-tsh-level-entry (measurement-entry serum-tsh-level
                                       #(rand-range 0.005 5) "mU/L")
   :cardiovascular-disease-diagnosis (entry cardiovascular-disease)
   :bp-elevated-result nil
   :bp-normal-result nil
   :bp-sys-normal (measurement-entry systolic-bp
                                     #(rand-range 80 130) "mmHg")
   :bp-dia-normal (measurement-entry diastolic-bp
                                     #(rand-range 40 85) "mmHg")
   :bp-sys-elevated (measurement-entry systolic-bp
                                       #(rand-range 131 190) "mmHg")
   :bp-dia-elevated (measurement-entry diastolic-bp
                                       #(rand-range 86 100) "mmHg")
   :bp-normal-result-with-resolved nil
   :hypertension-resolved-node (entry hypertension-resolved)
   :blood-pressure-reading-stage-2 nil
   :blood-pressure-reading-stage-1 nil
   :blood-pressure-reading-checkup nil
   :hypertension-diagnosis-1 (entry hypertension)
   :hypertension-diagnosis-2 (entry hypertension)
   :hypertension-management-upgrade nil
   :asprin-warfarin (entry (concat aspirin warfarin))
   :other-statins (entry (concat rosuvastatin-pravastatin atorvastatin-fluvastatin))
   :simvastatin-node (entry simvastatin)
   :statins-node nil
   :general-prescriptions nil
   :prescribe-ace-inhibitors nil
   :ace-V (entry ramipril-ramipril-with-felodipine)
   :ace-W (entry (concat captopril cilazapril enalapril-maleate fonsinopril-sodium))
   :ace-X (entry (concat qinapril-trandolapril lisinopril))
   :calcium-channel-blockers (entry calcium-channel-blocker)
   :central-alpha-antogonists (entry (concat moxonidine clonidine))
   :thiazides (entry (concat chlortalidone indapamide))
   :angiotensin-ii-receptor-antagnoist (entry (concat candesartan eprosartan-irbesartan iosartan-olmesartan telmisartan-valsartan))
   :beta-blockers (entry (concat oxprenolol-pindolol acebutolol-celiprolol atenolol-nadolol-sotalol bisoprolol-metoprololnebivolol propranolol-hydrochloride))
   :stage-2-presecriptions nil
   })



