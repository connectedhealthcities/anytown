(ns simulator.pathways
  "Utilities for defining simulation pathways."
  (:use [clojure.data priority-map]
        [clojure.algo.generic.functor :only (fmap)]))

(defn node
  "Create a pathway node.
  A node consists of:
  update-fn - function that takes the current future node list of a lifeline
              and returns a (possibly) modified node list.
  event     - event recorded as having happened when this node is activated.
  children  - representing possible choices that are inserted into the future
              lifeline once this node is activated.
  tags      - tags associated with this node. Used for identifying node in
              update functions."
  [update-fn event children tags]
  {:update-fn update-fn
   :event event
   :children children
   :tags tags})

(defn child
  "Create a child of a pathway node.
   A child consists of:
   node   - the node that will be inserted into the future lifeline if this
            child is chosen.
   weight - the weighting assigned to this child, used to determine probability
            of child being chosen.
   time   - the relative (not absolute) time after the the parent node that
            this child (if chosen) will trigger."
  [child weight time]
  {:node node
   :weight weight
   :time time})

(defn lifeline
  "Lifeline consists of a past, events that have happened, and a future, events
   that will happen. Future events are defined as nodes. Each future node has a
   time (relative to 'now') at which it will happen.

   This function is called with node time pairs, used to construct priority map
   of future nodes."
  [& node-times]
  {:past nil
   :future (apply priority-map node-times)})
