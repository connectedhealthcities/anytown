(ns ^{:doc "Simulation pathway definitions and core functions."
      :author "James Cunningham"}
  simulator.pathways  
  (:use [clojure.data priority-map]
        [simulator.utils]))

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
            child is chosen OR if a sequence, a list of possibilities.
   weight - the weighting assigned to this child, used to determine probability
            of child being chosen.
   time   - the relative (not absolute) time after the the parent node that
            this child (if chosen) will trigger."
  [node weight time]
  {:node node
   :weight weight
   :time time})

(defn possibility
  "Represents a possible choice for a node.
  option - the node to be chosen.
  chance - the (absolute) chance that this node is chosen i.e. more than one
           node can be chosen given a list of possibilities."
  [option chance]
  {:node option
   :chance chance})

(defn weighted
  "A weighted choice - currently used to provide options in a 
  journal map (needs tidying)."
  [choice weight]
  {:weight weight :choice choice})

(defn- resolve-fn
  "Evaluate if a function."
  [f] (if (fn? f) (f) f))

(defn create-lifeline
  "Lifeline consists of a past, events that have happened, and a future, events
  that will happen. Future events are defined as nodes. Each future node has a
  time (relative to 'now') at which it will happen. The past is a reverse
  ordered list of facts, with the time being relative to the previous fact
  (or, for the first fact, relative to patient birth date).
  This function is called with node time pairs, used to construct priority map
  of future nodes."
  [& node-times]
  {:past nil
   :future (apply priority-map (map resolve-fn node-times))})

(defn fact
  "A fact is an event that has happened at a given time.
  A fact consists of:
  event - the event recorded as happening.
  time  - a timestamp for this fact."
  [event time]
  {:event event
   :time time})

(defn- choices-from-possibilities
  "Given a list of possibilities, choose a selection."
  [possibilities time]
  (apply hash-map (interleave
                   (map :node (filter #(<= (rand) (:chance %)) possibilities))
                   (repeat time))))

(defn- choose-from
  "Selects child node(s) from an activated node."
  [node]
  ;; will add more complex child choices
  (let [choice (weighted-choice weight (:children node))]
    (cond (nil? choice) nil
          (sequential? (:node choice)) (choices-from-possibilities
                                        (:node choice) (:time choice))
          :else {(:node choice) (resolve-fn (:time choice))})))

(defn advance-lifeline
  "Takes the next node from the future of the life line, records it as a fact
  in the past and inserts children from it into the future if applicable."
  [lifeline]
  (if (empty? (:future lifeline)) lifeline
      (do ;;(println "===" (:event (first (peek (:future lifeline)))) "\n\n\n")
        (let [[next time] (peek (:future lifeline))
              future (pop (:future lifeline))
              choice (choose-from next)]
          {:past (cons (fact (:event next) time) (:past lifeline))
           :future (into ((:update-fn next) (fmap' #(- % time) future))
                         choice)}))))

(defn run-lifeline
  "Advances a lifeline until the future is empty or the (optional) 
  stop condition is true."
  ([lifeline] (run-lifeline lifeline (fn [_] false)))
  ([lifeline stop?]
     (letfn [(empty-or-stop? [ll] (or (empty? (:future ll)) (stop? ll)))]
       (loop [l lifeline] (if (empty-or-stop? l) l
                              (recur (advance-lifeline l)))))))
