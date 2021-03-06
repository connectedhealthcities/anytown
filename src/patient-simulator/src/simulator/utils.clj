(ns ^{:doc "Utility functions."
      :author "James Cunningham"}
  simulator.utils
  (:use [clojure.algo.generic.functor :only (fmap)]
        [clojure.core.matrix.random :only (sample-normal
                                           sample-binomial)]))

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

(defn weight
  "Returns either :weight of an item or 1 if weight is nil."
  [item] (or (:weight item) 1))

(defn fmap'
  "A version of fmap that returns nil if s is empty."
  [f s]
  (if (not (empty? s)) (fmap f s)))

(defn rand-normal
  [mean sd]
  (+ mean (* sd (first (sample-normal 1)))))

(defn rand-binomial
  [n p]
  (* n (first (sample-binomial 1 p))))

(defn rand-range
  "Returns a random real value in the range [x y)."
  [x y] (+ x (rand (- y x))))



