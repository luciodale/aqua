(ns aqua.core
  (:require
   [aqua.utils :as utils]))

(defn subscribe
  "Entry point to register one or many animations."
  [& animation]
  (doseq [one animation]
    (utils/subscribe-effect one)))
