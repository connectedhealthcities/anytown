(ns ^{:doc "Pathway manipulation utilities."
      :author "James Cunningham"}
  simulator.pathways.utils
  (:use [simulator.pathways]))

(defn past
  "Returns the past of a lifeline in chronological order."
  [lifeline]
  (reverse (:past lifeline)))

(defmacro possibilities
  "Creates a list of possibilities from node/probability pairs."
  [& node-probabilities]
  (let [paired (partition 2 node-probabilities)]
    `(list ~@(map #(list 'possibility (first %) (second %)) paired))))

(defmacro all-of
  "Choose all of the given child nodes. Creates a possibility list with
  all probabilities set to 1."
  [& nodes]
  `(list ~@(map #(list 'possibility % 1) nodes)))

(defn create-removed-tagged-nodes-fn
  "Creates a function, intended for use as lifeline update function, which
  removes any nodes from the future tagged with any of tags."
  [tags]
  (fn [f] (filter #(not (some (zipmap tags (repeat true)) (:tags %))) f)))

(defn make-simple-node
  "Convenience function for creating a simple node.

  Creates a node for event with identity update function and
  (optionally) children and tags."
  ([event children & tags] (node identity event children tags))
  ([event] (make-simple-node event nil)))

(defmacro def-simple-node
  "Defines a simple node.

  Def's a node with name name, the event for this node is created with
  (keyword (str name)). Children and tags are optional."
  ([name children & tags]
     `(let [event# ~(keyword (str `~name))]
        (def ~name (make-simple-node event# ~children ~@tags))))
  ([name] `(def-simple-node ~name nil)))

(defn make-repeating-node
  "Creates a node with an update function that inserts a copy of itself
  into the lifeline future at a time determined by time-fn."
  ([time-fn event children & tags]
     (letfn [(repeater [future]
               (assoc future (apply make-repeating-node
                                    (concat (list time-fn event children) tags))
                      (time-fn)))]
       (node repeater event children tags)))
  ([time-fn event] (make-repeating-node time-fn event nil)))

(defn make-regular-repeating-node
  "Creates a node with an update function that inserts a copy of itself
  into the lifeline future at a time determined by time-fn. The call to
  time-fn is memoized so that all future insertions happen at regular
  intervals."
  ([time-fn event children & tags]
     (apply make-repeating-node
            (concat (list (memoize time-fn) event children) tags)))
  ([time-fn event]
     (make-regular-repeating-node time-fn event nil)))

(defn make-limited-repeating-node
  "Creates a node with an update function that inserts a copy of itself
  into the lifeline future at a time determined by time-fn if time-limit
  is greater than 0. The inserted copy has a time-limit of 
  (- time-limit time) where time is the time into the future that the node
  was inserted."
  ([time-fn time-limit event children & tags]
     (letfn [(repeater [future]
               (let [time (time-fn)
                     remaining (- time-limit time)]
                 (if (> remaining 0)
                   (assoc future (apply make-limited-repeating-node
                                        (concat
                                         (list time-fn remaining event children)
                                         tags))
                          time)
                   future)))]
       (node repeater event children tags)))
  ([time-fn time-limit event]
     (make-limited-repeating-node time-fn time-limit event nil)))

(defn make-regular-limited-repeating-node
  "Creates a node with an update function that inserts a copy of itself
  into the lifeline future at a time determined by time-fn if time-limit
  is greater than 0. The inserted copy has a time-limit of
  (- time-limit time) where time is the (memoized) time into the future
  that the node was inserted. time-fn is initially memoized and used in
  future node insertions."
  ([time-fn time-limit event children & tags]
     (apply make-limited-repeating-node
            (concat (list (memoize time-fn) time-limit event children) tags)))
  ([time-fn time-limit event]
     (make-regular-limited-repeating-node time-fn time-limit event nil)))

(defn make-death-node
  "Creates a node with an update function which clears the future of
  lifeline. If event is not given the inserted event is :death.

  Intended to be used as a death event. (Note: run-lifeline function
  terminates on an empty future)."
  ([event] (node (fn [_] nil) event nil nil))
  ([] (make-death-node :death)))


