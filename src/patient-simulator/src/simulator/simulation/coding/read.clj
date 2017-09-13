(ns ^{:doc "Read code definitions."
      :author "James Cunningham and Lucy Bridges"}
  simulator.simulation.coding.read
  (:use [simulator.utils :only (rand-range rand-normal rand-binomial)]
        simulator.simulation.utils))

;; ethnicities

(def ethnicity-white
  ["9S1.." "9S1-." "9i0.." "9i1.." "9i2.." "XaJQv" "XaJQw"])

(def ethnicity-asian
  ["XaFwz" "XaJR2"   "XaJR3" "XaJR4" "9i7.." "9i8.." "9i9.." "9iA.." "9iA-." "9S6.." "9S7.." "9S8.." ])

(def ethnicity-black
  ["XaFwH" "XaJR6" "XaJR7" "9iB.." "9iC.." "9iD.." "9iD0." "9iD1." "9iD2." "9iD3." "9iD4." "9S2.." "9S3.." "9S4.." "9S4-." ])

(def ethnicity-mixed
  [ "XaFwG" "XaJR0" "XaJQy" "XaJQz" "9i3.." "9i4.." "9i5.." "9i6.." "9i60." "9i61." "9i62." "9i63." "9i64." "9i65." "9S5.." "9S51." "9S52." ])

(def ethnicity-other
  [ "XaFx1" "9SA.." "XS7AU" "XaJR9" "9iE.." "9iF.." "9SA.." "9SA-."])

(def ethnicity-not-stated
  [ "9iG.." "XaJRB"])

(def ethnicity-all
  (concat ethnicity-white
          ethnicity-asian
          ethnicity-black
          ethnicity-mixed
          ethnicity-other))

;; Assorted conditions

(def hypertension ["G20.." "G2..." "G2y.." "XE0Ub" "G202." "XUTvj" "XUEwn" "G203."])

(def hypertension-resolved ["212K." "21261"])

(def chronic-kidney-disease ["1Z10." "1Z11." "1Z19." "1Z1A." "XaLHH" "1Z13." "1Z12." "1Z14." "1Z1B." "1Z16." "1Z15." "1Z1E." "1Z1L." "1Z1K." "1Z1H." "1Z1G."])

(def cardiovascular-disease ["G33.." "XUHaO" "G35.." "G30.." "XUSwL" "X200E" "Gyu3." "XUSqa" "X200C" "G3..." "G340." "G61.." "X00D1" "G667." "G668." "XE0Ww"])


;; General admin

(def pulse-rate ["242.."]) ;;TODO measurement values

(def had-a-chat ["8CB.."])

(def alcohol-screening ["38D4."]) ;;TODO measurement values

(def new-patient-screening ["68R2."])

(def smoking-cessation-advice ["8CAL."])

(def seen-by-gp ["9N21."])

(def general-admin
  (concat
    (measurement-entry pulse-rate #(rand-normal 70 50/120) "bpm")
    (entry had-a-chat)
    (measurement-entry alcohol-screening #(rand-normal 3 1/12) "/12")
    (entry new-patient-screening)
    (entry smoking-cessation-advice)
    (entry seen-by-gp)))


;; General medications

(def aspirin ["bu23." "di11." "di15." "di17." "di1g." "di1i." "di1f." "za0yj" "za0yi" "za0yt" "XUWZe" "XUBAv" "XUBAw" "XUBAx" "XUBAy" "XUBAz"])

(def paracetamol ["di21." "za161" "di21." "di21." "di21."])

(def omeprazole ["a6b1." "a6b1." "a6b1." "a6b1." "a6b1."])

(def penicillin ["e161." "e162." "e163." "e16z." "e441." "e442." "e443." "e44z." "e311." "e312." "e313." "e314." "e315." "e31A." "e31b." "e322." "e323." "e324." "e32p." "e32q." "e32w." "e421." "e422." "e42y." "e42z." "e151." "e152." "e153." "e154." "e155." "e156." "e411." "e412." "e413." "e414." "e415." "e416." "e41w." "e41x." "e221." "e222." "e223." "e229." "e22a." "e22e." "e22l." "e241." "e242." "e243." "e244." "e245." "e351." "e35y." "e35z." "e371." "e372." "e373." "e374." "e375." "e376." "e377." "e37v." "e37y." "e37z." "e361." "e362." "e363." "e364." "e365." "e36z." "e511." "e512." "e513." "e514." "e381." "e382." "e383." "e384." "e385." "e386." "e387." "e388." "e389." "e113." "e114." "e33.." "e33f." "e3zb." "e3zB." "e3zc." "e3zC." "e3zy." "e3zz." "e43.." "e52.."])

(def other-antibiotics ["mc7.." "e76y." "e771." "e772." "e713." "e761." "e94.." "e91.." "e92.."])

(def general-medications
  (concat
    (entry aspirin)
    (entry paracetamol)
    (entry omeprazole)
    (entry penicillin)
    (entry other-antibiotics)))


;; General measurements

(def flu-vaccination ["9OX.." "XaW4N" "n47.." "65E20" "65ED0" "XaZ0d" "XaZ0e" "XaZfY"])   ;; used by asthma and diabetes

(def systolic-bp ["2469."])

(def diastolic-bp ["246A."])

(def height ["229.."])

(def weight ["22A.."])

(def bmi ["22K.."])

(def general-measurements
  (concat
    (measurement-entry bmi
            #(rand-normal 24 18/40) "kg/m^2")
    (measurement-entry weight
            #(rand-normal 90 40/150) "kg")
    (measurement-entry height
            #(rand-normal 185 120/250) "cm") 
    (measurement-entry systolic-bp
            #(rand-range 80 130) "mmHg") 
    (measurement-entry diastolic-bp
            #(rand-range 40 85) "mmHg")))


(def new-patient-details
  (concat
    (measurement-entry alcohol-screening #(rand-normal 2 1/12) "/12")
    (entry new-patient-screening)
    (entry smoking-cessation-advice)
    (measurement-entry bmi
            #(rand-normal 24 18/40) "kg/m^2")
    (measurement-entry weight
            #(rand-normal 90 40/150) "kg")
    (measurement-entry height
            #(rand-normal 185 120/250) "cm") 
    (measurement-entry systolic-bp
            #(rand-range 80 130) "mmHg") 
    (measurement-entry diastolic-bp
            #(rand-range 40 85) "mmHg")
    ))


;; General blood tests
(def serum-ldl-cholesterol ["44P6."])
  
(def serum-hdl-cholesterol ["44P5."]) 

(def serum-cholesterol-and-level ["44P.."])

(def creatinine-serum ["44J3."]) 

(def potassium-serum ["44l4."]) 
   
(def sodium-serum ["44l5."]) 
   
(def urea-serum ["44J9."])

(def haemoglobin ["423.."]) 

(def mcv ["42A.."]) 
   
(def rbc ["426.."]) 

(def wbc ["42H.."])

(def plt ["42P.."]) 

(def albumin-serum ["44M4."]) 

(def alkaline-phosphatase ["44F.."]) 

(def total-bilirubin ["44EC."])

(def total-protein ["44M3."]) 

(def general-blood-tests
  (concat
    (measurement-entry serum-ldl-cholesterol #(rand-binomial 9.00 28/90) "mmol/L")
    (measurement-entry serum-hdl-cholesterol #(rand-binomial 2.5 14/25) "mmol/L")
    (measurement-entry serum-cholesterol-and-level #(rand-binomial 6.00 5/6) "mmol/L")
    (measurement-entry creatinine-serum #(rand-range 0.1 16) "umol/L")
    (measurement-entry potassium-serum #(rand-range 0.1 12) "mmol/L")
    (measurement-entry sodium-serum #(rand-range 0.2 200) "mmol/L")
    (measurement-entry urea-serum #(rand-range 0.2 12) "mmol/L")
    (measurement-entry haemoglobin #(rand-range 0.1 16) "g/dL")
    (measurement-entry mcv #(rand-range 0.2 175) "f/L")
    (measurement-entry rbc #(rand-range 0.1 12) "x10^12/l")
    (measurement-entry wbc #(rand-range 0.01 12) "x10^9/l") 
    (measurement-entry plt #(rand-range 0.01 30) "x10^9/l")
    (measurement-entry albumin-serum #(rand-range 0.1 60) "g/L") 
    (measurement-entry alkaline-phosphatase #(rand-range 0.4 120) "U/L")
    (measurement-entry total-bilirubin #(rand-range 1 24) "umol/L")
    (measurement-entry total-protein #(rand-range 0.05 9) "g/L")))


;; Cardiovascular tests

(def serum-cholesterol-normal ["44P1."])
(def serum-cholesterol-raised ["44P3."])
(def serum-cholesterol-very-high ["44P4."])
(def urine-protein-test ["467.."])
(def urine-albumin-level ["46N4."])
(def blood-urea-renal-function ["44J.."])
(def corrected-calcium ["44lC."])
(def phosphate-serum ["44l9."])
(def alt ["44G3."])
(def estimated-glomerular-filtration-rate ["451E."])
(def thyroid-function-test ["442J."])
(def serum-tsh-level ["442W."])


;; Cardiovascular drugs

(def warfarin ["8B2K." "bs17." "bs18." "bs19." "bs14." "bs15." "bs16." "bs1A." "x01O3." "x01O5." "za0Qc" "za0Qd" "za0Qe" "za18m"])

(def ramipril-ramipril-with-felodipine ["bi63." "bi6C." "za0SU" "za15f" "za0p8" "bi66." "bi6u." "bi69." "bi66." "bi6u." "bi6t." "bi61." "bi6q." "bi6r." "bi6E." "bi6B." "bi6D." "bi6w." "bi6x." "bi6z." "za1Dg" "za1Dh" "bA1z." "bA1y." "bA12."])

(def captopril ["bi1w." "bi1x." "bi1z." "bi1y."])

(def cilazapril ["bi83." "bi84." "bi82." "bi8a."])

(def enalapril-maleate ["bi2w." "bi2x." "bi2C." "bi2t." "bi2A." "bi2y." "bi2u." "bi2v."])

(def fonsinopril-sodium ["za0T4" "za0T5" "XUVbK" "XUVbJ" "XUbGI" "XUbGJ"])

(def lisinopril ["za0SP" "za0SQ" "za13A" "za0u7" "XUVOT" "XUVOS" "za0U8" "za0ps" "x007H"])

(def qinapril-trandolapril ["za0SR" "za0p7" "za0SS" "za0ST" "bi43." "bi48." "bi4B." "bi4C." "bi4D." "bi4E." "bi42." "bi47." "bi4A." "bi45." "bi49." "bi41." "bi44." "bi46." "bi91." "bi96." "bi93." "bi97." "bi94." "bi9z." "bi9A." "za0Sa" "za0SZ" "za0u9"])

;;(def centrally-acting-antihypertensive ["drugs"])

(def moxonidine ["bf41." "bf42." "za0au" "bf45." "za0av" "bf46." "bf43." "bf44."])

(def clonidine ["bf1z." "bf1y." "bf1z." "bf1w." "bf11." "bf12."])

(def candesartan ["bk7.." "za0w2" "za176" "za0w3" "za0w4"])

(def eprosartan-irbesartan ["bk5.." "za0vY" "za0vZ" "za0va" "bk9.." "za1Bp" "za1Bq" "za1Br"])

(def iosartan-olmesartan ["XUeb8" "XUb6Z" "XUb6b" "XUb6c"])

(def telmisartan-valsartan ["bk41." "bk42." "bk44." "bk45." "bk46." "bk4v." "bk47." "bk48.bk4y." "bk4x." "bk8z." "bk85." "bk81." "bk8x." "bk8y." "za1BM." "zaDN"])

(def chlortalidone ["b23y." "b231." "b232." "b23y." "b23z." "za0S4" "za0U3"])

(def indapamide ["b28z." "b285." "b289." "b288." "b28z." "b284." "b283." "b285." "x05df" "za0bm" "za0p2"])

(def oxprenolol-pindolol ["Bd8.." "za08T" "za08U" "za127" "za08V" "bd86." "bda2." "bdaz." "bda4." "bda3." "bda1." "bday." "za042" "za13x" "bdaz." "bday." "bdeG."])

(def acebutolol-celiprolol ["bd2y." "bd2x." "bd2z." "bd2w." "x004" "bdex." "za03u" "za03v" "za03w" "x0004" "bd2x."])

(def atenolol-nadolol-sotalol ["bd35." "bd3x." "bd3j." "bd3g." "bd3e." "bd3a." "bd33." "bd3x" "bd37." "bd3d." "bd3f." "bd36." "x03jn" "bdeP." "za040" "za041" "z0087" "bd71." "bd72." "bd7z." "bd7y" "za08h" "za08i" "za08j" "za08k" "x00B4" ""])

(def bisoprolol-metoprololnebivolol ["za14F" "za0tK" "za1Cz" "za1C0" "za1Bv" "bdf.." "za08H" "bdf8." "bdfz." "bdfy." "bdfx." "bdf2." "bdfw." "bdeA." "bdeB." "x007ibd6y." "bd6x." "bd6z." "za0tV" "za0gt" "XUeg0" "bdm2." "bdmy." "bdmz." "bdm1." "za18U"])

(def propranolol-hydrochloride ["bd11." "bd1x." "bd14." "x01CD" "bd1P." "za0pr" "bdn.." "bd12." "bdn6." "bd1Q." "bd1Y." "za0gr" "za0BA"])

(def simvastatin ["bxd5." "za0Sp" "za11G" "za0ab" "za1BW" "bxd8." "bxd4." "bxdD." "bxdE." "bxdF." "bxdG." "bxdz." "bxd6." "bxd1." "bxdu." "bxdv." "bxdK." "bxd2.." "bxd7." "bxdC." "bxd3." "bxdw." "bxdx." "bxdy." "bxdH." "bxdl." "bxdJ." "bxdA." "bxdB."])

(def rosuvastatin-pravastatin ["bxky." "bxkx." "bxkz." "bxkw." "bxe5." "bxe6." "bxe7."])

(def atorvastatin-fluvastatin ["bxi2." "bxi3." "bxi1." "bxiz." "bxgz." "bxg4." "bxg3." "za15j" "za0Th" "za1AH"])

(def calcium-channel-blocker ["bb3A." "x01QU" "za0Ta" "za0Tb" "x01CU" "za12A" "blb1." "blb2." "za047" "za11U" "bla2." "bla1." "za046" "XUWzp" "XUVb2" "za0e8" "za048" "ble2." "ble1." "ble3." "ble4."])

(def statins
  (concat 
      (entry simvastatin)
      (entry rosuvastatin-pravastatin)
      (entry atorvastatin-fluvastatin)))


;; asthma

(def asthma-diagnosis
  ["H33.." "XUSTF" "663V1" "H332." "663V2" "663V0" "XU1RC"])

(def asthma-management-plan
  ["663U." "66Y5." "66Y9." "66YA." "661M1"])

(def asthma-annual-review
  ["66YJ." "Xaleq" "66YK." "66YQ."])

(def asthma-resolved
  ["212G."])

(def spirometry-screening
  ["68M."])


;; asthma medications

(def terbutaline-prescription
  ["za0h2" "c14g." "za0BS" "x006b"])

(def perbuterol-prescription
  ["c16z." "c164."])

(def salbutamol-prescription
  ["za0Wq" "c13.." "c1E.." "c13v." "za0eH" "c13R." "za17j"])

(def prednisolone
  ["fe71." "fe72." "fe76."])

(def beclometasone-dipropionate ["c61z." "c61u." "c66d." "c66e." "c668." "c666." "c665." "c66c."])

(def budesonide ["c67.." "za1Ek" "za1Ej" "za1K7" "XUcfy" "XUcfz"])

(def fluticasone-propionate ["c1Dv." "c1Dw." "c1Dz." "c1dx." "c1Dy." "c1Du."])

(def mometasone-furoate ["C682." "c681."])

(def corticosteroids
  (concat
   beclometasone-dipropionate
   budesonide
   fluticasone-propionate
   mometasone-furoate))



(def salmeterol ["c19z." "c191."])
(def formoterol ["c1Cz." "c1C.." "za0vl"])

(def long-acting-beta-2
  (concat salmeterol formoterol))


;; Diabetes diagnosis

(def diabetes-type-1 ["C10.." "C10E." "X40J4" "C108." "C1085" "C1086" "C1089" "L1805" "C108E" "XaFWG" "C10EE"])

(def diabetes-type-2 ["C10.." "C10F.." "X40J5" "C109J" "C109K" "C109." "C1094" "C1095" "C10F9" "XaELQ" "C1001"])


;; Diabetes medications

(def insulin ["XU6gK" "XU6gQ" "za0le" "za0Lk" "za0Ze" "za0Zf" "f11.." "f111." "f112." "f12.." "f13.." "f14.." "f15.." "fw21." "fw22." "XU6gD" "XU6gJ" ])

(def metformin ["f411." "f412." "f413." "f414." "f415." "f416." "f417." "f418." "f419." "f41A." "f41B." "f41C." "f41D." "f41E." "f41F." "f41G." "f41s." "f41t." "f41u." "f41v." "f41w." "f41x." "f41y." "f41z."])

(def sulphonylureas ["f36.." "f39.." "f31.." "f33.." "f3A.." "f32.." "za19q" "XM0lF" "za0Lp" "za0Lq" "za0Lr" "za16f" "f34.." "f35.." "za0Ls" ])

(def alpha-glucosidase-inhibitors ["za0m3" "za12s" "XUV2g" "XUV2h" "XUWZm" "XUarg" "XUatQ" "XUfmM" "ft11." "ft12." "ft13." "ft14."])

(def prandial-glucose-regulators ["za1zk" "za1zl" "za1zm" "XUiTE" "ft31." "ft32." "ft33." "ft34." "ft35." "ft36." "ft37." "ft38." "ft39." "XUPo8" "ft6.." "za1EO"])

(def glitazones ["ft21." "ft22." "ft23." "ft24." "ft25." "ft26."  "za19q" "f35.." "za0Ls" "f351." "f352." "f353." "f354." "f355." "f356." ])

(def gpl-1-analogues ["XUd1Z" "XUd1X" "XUd1Y" "XUhv9" "XUiFv" "XUjpF" "XUjqV" "ftc1." "ftc2." "ft91." "ft92." "ft93." "ft9x." "ft9y." "ft9z."])

(def dpp-4-inhibitors ["ft81." "ft82." "ft83." "ft8x." "ft8y." "ft8z." "ftd1." "ftd2." "ftdy." "ftdz." "fta1." "ftaZ." "XUg0s" "XUfbP" "XUfg0" "XUkro" ])


;; Diabetes monitoring

(def fasting-blood-glucose-level ["44T2." "44TK."])   ;;0.9-10 mmol/L 

(def serum-glucose ["44f.."])   ;;1.4-9 mmol/L

(def serum-random-glucose ["44f0."])   ;;1.4-9 mmol/L

(def random-blood-glucose-level ["44T1."])   ;;1-13 mmol/L

(def total-cholesterol ["44OE." "44PH." "44PJ." "44PK." "XaIqd" "XaJe9"])   ;;?? mmol/L

(def education-plan-referral ["9OLM." "XaNTH" "XaXkZ"])

(def hba1c-ifcc ["42W5." "XaPbt"])   ;; diabetic avg 42-75 mmol/mol

(def retinal-screening ["2BB.." "3128." "3129." "312E." "312G." "58C1." "68A7." "68A8." "66AD." "8HBD." "8HBG." "8HBH." "9N1v." "9N2U." "9N2V." "9N2e." "9N2f." "9NNC." "Xa1zl" "XaEJQ" "XaIIj" "XaIPm" "XaATf" "XaIPm" "XaJLa" "XaJLb" "XaJO7"]) 

(def foot-examination ["66Ab" "8I6G." "8I3W." "8IAo." "8IB6." "66AE." "XUZFR" "XaWR5" "XaWR7"]) 

(def microalbuminuria-testing ["46W.." "46N4." "XUBFx" "XUERW"])

(def neuropathy-testing ["8I6G." "8I3W." "8IAn." "8IB5." "XaJOE" "XaJix" "XaWR4" "XaWR6"])

(def diabetic-dietary-review ["66At." "66At0" "66At1" "XaX3o"])

(def foot-risk-classification ["2G5E." "2G5F." "2G5G." "2G5H." "2G5I." "2G5J." "2G5K." "2G5L." "2G5d." "2G5e"])


;; Diabetic risk factors

(def obesity ["22K5." "22K.."]) ;; TODO >30 kg/m^2

(def retinopathy ["F4201" "2BBF." "F420z" "XaJOg" "XUYwr" "XaJOh" "XaKcS" "2BBQ" "2BBP." "2BBo."])

(def foot-amputation ["7L07." "2G42." "2G44." "2G46." "XaBLT" "XaBLV" "XaBLX" "2G43." "2G45." "2G47." "XaBLU" "XaBLW" "XaBLY"]) 

(def neuropathy ["X00Ag" "XUf2H" "F3720" "F350." "F1711" "F3721" "XUSkQ" "XUFpN" "F3410"])

(def proteinuria ["R110." "R110z" "C10EK" "C10FL" "K190X" "Kyu5G" "K08yA"])


;; Other

(def all-assorted 
  (concat general-measurements general-medications general-admin))


