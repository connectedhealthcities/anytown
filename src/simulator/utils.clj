(ns simulator.utils)

(defn weighted-choice
  "Selects an element from choices weighted by the value extracted from
   each element by weight-fn. If no weight-fn is given each element is
   weighted equally."
  ([weight-fn choices]
     (letfn [(f [[total choice] next]
               (let [weight (weight-fn next)
                     total (+ total weight)]
                 [total (if (< (rand) (/ weight total)) next choice)]))]
       (second (reduce f [0 nil] choices))))
  ([choices] (weighted-choice (fn [_] 1) choices)))
