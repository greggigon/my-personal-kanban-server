(ns my-personal-kanban-local-cloud.configuration)

(def default-configuration {:port 8080 :directory (str (System/getProperty "user.home") "/" "mpk")})
(def configuration (ref default-configuration))


(defn number-if-one [some-string]
  (try
    (Integer/parseInt some-string)
    (catch NumberFormatException e some-string)))

(defn vectors-to-map [pairs the-map]
  (if (empty? pairs)
    the-map
    (let [[a-key value] (first pairs)]
       (vectors-to-map (rest pairs) (merge the-map {(keyword a-key) (number-if-one value)})))))



(defn do-build-configuration
  "Build configuration map from command line parameters"
  [parameters]
  (let [new-configuration (vectors-to-map (partition 2 parameters) {})]
    (dosync (ref-set configuration (merge default-configuration new-configuration)))))
