(ns ^{:doc "Pathway manipulation utilities."
      :author "James Cunningham"}
  simulator.pathways.utils
  (:use clojure.data.priority-map
        [simulator.pathways]))

(defn past
  "Returns the past of a lifeline in chronological order."
  [lifeline]
  (reverse (:past lifeline)))

(defmacro children
  "Generates (list (child node weight time)...) from 
  list of node weight time's.
  e.g. (children node1 1 0
                 node2 2 100)
  note: ignores the last |child-info| mod 3 arguments."
  [& child-info]
  (let [info (partition 3 child-info)]
    `(list ~@(map #(list 'child (first %) (second %) (nth % 2)) info))))

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

(defn create-scrub-tags-fn
  "Creates a function, intended for use as lifeline update function, which
  removes any nodes from the future tagged with any of tags."
  [tags]
  (fn [f]
    ;; probably not the best way to do this
    (apply priority-map
           (flatten
            (filter #(not (some (zipmap tags (repeat true)) (:tags %))) f)))))

(defn make-scrub-tags-node
  "Creates a node which when activated scrubs all nodes tagged with any of
  tags-to-remove from the future of the lifeline."
  ([event tags-to-remove children & tags]
     (node (create-scrub-tags-fn tags-to-remove)
           event children tags))
  ([event tags-to-remove]
     (make-scrub-tags-node event tags-to-remove nil)))

(defmacro scrub-tags-node
  "Defines a simple node with name via make-scrub-tags-node
  (where event is (keyword (str name)))."
  ([name tags-to-remove children & tags]
     `(let [event# ~(keyword (str `~name))]
        (def ~name (make-scrub-tags-node event# ~tags-to-remove
                                         ~children ~@tags))))
  ([name tags-to-remove] `(scrub-tags-node ~name ~tags-to-remove nil)))

(defn make-simple-node
  "Convenience function for creating a simple node.
  Creates a node for event with identity update function and
  (optionally) children and tags."
  ([event children & tags] (node identity event children tags))
  ([event] (make-simple-node event nil)))

(defmacro simple-node
  "Defines a simple node with name via make-simple-node 
  (where event is (keyword (str name)))."
  ([name children & tags]
     `(let [event# ~(keyword (str `~name))]
        (def ~name (make-simple-node event# ~children ~@tags))))
  ([name] `(simple-node ~name nil)))

(defn make-repeating-node
  "Creates a node with an update function that inserts a copy of itself
  into the lifeline future at a time determined by time-fn."
  ([event time-fn children & tags]
     (letfn [(repeater [future]
               (into future {(apply make-repeating-node
                                    (concat (list event time-fn children) tags))
                             (time-fn)}))]
       (node repeater event children tags)))
  ([event time-fn] (make-repeating-node time-fn event nil)))

(defmacro repeating-node
  "Defines a repeating node with name via make-repeating-node 
  (where event is (keyword (str name)))."
  ([name time-fn children & tags]
     `(let [event# ~(keyword (str `~name))]
        (def ~name (make-repeating-node event# ~time-fn ~children ~@tags))))
  ([name time-fn] `(repeating-node ~name ~time-fn nil)))

(defn make-regular-repeating-node
  "Creates a node with an update function that inserts a copy of itself
  into the lifeline future at a time determined by time-fn. The call to
  time-fn is memoized so that all future insertions happen at regular
  intervals."
  ([event time-fn children & tags]
     (apply make-repeating-node
            (concat (list (memoize time-fn) event children) tags)))
  ([event time-fn]
     (make-regular-repeating-node time-fn event nil)))

(defmacro regular-repeating-node
  "Defines a regular repeating node with name via make-regular-repeating-node
  (where event is (keyword (str name)))."
  ([name time-fn children & tags]
     `(let [event# ~(keyword (str `~name))]
        (def ~name (make-regular-repeating-node event# ~time-fn
                                                ~children ~@tags))))
  ([name time-fn] `(regular-repeating-node ~name ~time-fn nil)))

(defn make-limited-repeating-node
  "Creates a node with an update function that inserts a copy of itself
  into the lifeline future at a time determined by time-fn if time-limit
  is greater than 0. The inserted copy has a time-limit of 
  (- time-limit time) where time is the time into the future that the node
  was inserted."
  ([event time-fn time-limit children & tags]
     (letfn [(repeater [future]
               (let [time (time-fn)
                     remaining (- time-limit time)]
                 (if (> remaining 0)
                   (into future {(apply make-limited-repeating-node
                                        (concat
                                         (list event time-fn remaining children)
                                         tags))
                                 time})
                   future)))]
       (node repeater event children tags)))
  ([event time-fn time-limit]
     (make-limited-repeating-node time-fn time-limit event nil)))

(defmacro limited-repeating-node
  "Defines a repeating node with name via make-limited-repeating-node 
  (where event is (keyword (str name)))."
  ([name time-fn time-limit children & tags]
     `(let [event# ~(keyword (str `~name))]
        (def ~name (make-limited-repeating-node event# ~time-fn ~time-limit
                                                ~children ~@tags))))
  ([name time-fn time-limit] `(limited-repeating-node ~name ~time-fn ~time-limit
                                                      nil)))

(defn make-regular-limited-repeating-node
  "Creates a node with an update function that inserts a copy of itself
  into the lifeline future at a time determined by time-fn if time-limit
  is greater than 0. The inserted copy has a time-limit of
  (- time-limit time) where time is the (memoized) time into the future
  that the node was inserted. time-fn is initially memoized and used in
  future node insertions."
  ([event time-fn time-limit children & tags]
     (apply make-limited-repeating-node
            (concat (list (memoize time-fn) time-limit event children) tags)))
  ([event time-fn time-limit]
     (make-regular-limited-repeating-node time-fn time-limit event nil)))

(defmacro regular-limited-repeating-node
  "Defines a repeating node with name via make-regular-limited-repeating-node 
  (where event is (keyword (str name)))."
  ([name time-fn time-limit children & tags]
     `(let [event# ~(keyword (str `~name))]
        (def ~name (make-regular-limited-repeating-node
                    event# ~time-fn ~time-limit ~children ~@tags))))
  ([name time-fn time-limit]
     `(regular-limited-repeating-node ~name ~time-fn nil)))

(defn make-death-node
  "Creates a node with an update function which clears the future of
  lifeline. If event is not given the inserted event is :death.
  Intended to be used as a death event. (Note: run-lifeline function
  terminates on an empty future)."
  ([event] (node (fn [_] nil) event nil nil))
  ([] (make-death-node :death)))


