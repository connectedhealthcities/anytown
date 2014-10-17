(ns ^{:doc "Library for defining, querying and manipulating dates
           and times."
      :author "James Cunningham"}
  simulator.time
  (:use [incanter.distributions :only (normal-distribution uniform-distribution draw)])
  (import [java.util Date Calendar]
          [java.text SimpleDateFormat]))

;;; --- conversion functions ---

(defn date-in-ms
  "Get the time in milliseconds between epoch and given date.
  If no date is given default is now."
  ([] (.getTime (.getTime (Calendar/getInstance))))
  ([date] (.getTimeInMillis (doto (Calendar/getInstance) (.setTime date)))))

(defn years-to-ms
  "Naive conversion of years to milliseconds"
  [years] (* years 365 24 60 60 1000))

(defn months-to-ms
  "Naive conversion of months to milliseconds"
  [months] (* months 30 24 60 60 1000))

(defn weeks-to-ms
  "Naive conversion of weeks to milliseconds"
  [weeks] (* weeks 7 24 60 60 1000))

(defn days-to-ms
  "Naive conversion of days to milliseconds"
  [days] (* days 24 60 60 1000))

(defn hours-to-ms
  "Naive conversion of hours to milliseconds"
  [hours] (* hours 60 60 1000))

(defn year-in-ms
  "Converts a calendar year to milliseconds since epoch."
  [year] (.getTimeInMillis (doto (Calendar/getInstance)
                             (.setTimeInMillis 0)
                             (.set Calendar/YEAR year))))

;;; --- utility functions ---

(defn get-year
  "Returns a string representing the year of the given date in yyyy format."
  [date]
  (.format (SimpleDateFormat. "yyyy") date))

(defn after-now?
  "Returns true if the given date is in the future."
  [date]
  (.after date (Date.)))

(defn add-time-to-date
  "Adds a time specified in milliseconds to the given java.util.Date object."
  [date time]
  (Date. (+ time (date-in-ms date))))

;;; --- random range functions ---

(defn hours
  "Gives a random number of milliseconds representing an absolute
  value of between from and to hours."
  [from to]
  (+
   (hours-to-ms from)
   (rand-int (hours-to-ms (- to from)))))

(defn days
  "Gives a random number of milliseconds representing an absolute
  value of between from and to days."
  [from to]
  (+
   (days-to-ms from)
   (rand-int (days-to-ms (- to from)))))

(defn weeks
  "Gives a random number of milliseconds representing an absolute
  value of between from and to weeks."
  [from to]
  (+
   (weeks-to-ms from)
   (long (* (rand) (weeks-to-ms (- to from))))))

(defn months
  "Gives a random number of milliseconds representing an absolute
  value of between from and to months."
  [from to]
  (+
   (months-to-ms from)
   (long (* (rand) (months-to-ms (- to from))))))

(defn years-normal
  "Gives a random number of milliseconds representing an absolute
  value of between from and to years drawn from a normal distribution
  with given mean and sd number of years."
  [mean sd]
  (long (draw (normal-distribution (years-to-ms mean) (years-to-ms sd)))))

(defn years
  "Gives a random number of milliseconds representing an absolute
  value of between from and to years."
  [from to]
  (+
   (years-to-ms from)
   (long (* (rand) (years-to-ms (- to from))))))

(defn years-normal
  "Gives a random number of milliseconds representing an absolute
  value of between from and to years drawn from a normal distribution
  with given mean and sd number of years."
  [mean sd]
  (long (draw (normal-distribution (years-to-ms mean) (years-to-ms sd)))))


;;; --- random date functions ---
;; maybe consider moving (not time)

(defn random-date
  "Picks a random date (uniformly) between two years.
  (from/to 0hrs on 1/1 of year)"
  [from-year to-year]
  (new Date (+ (year-in-ms from-year)
               (long (rand (- (year-in-ms to-year)
                              (year-in-ms from-year)))))))

(defn random-date-normal
  [mean-year sd]
  (new Date
       (long (draw (normal-distribution (year-in-ms mean-year)
                                        (years-to-ms sd))))))
