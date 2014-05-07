(ns my-personal-kanban-local-cloud.core
  (:gen-class)
  (:use ring.middleware.params
        ring.util.response
        ring.adapter.jetty
        my-personal-kanban-local-cloud.configuration))

(defn read-kanban [parameters]
  "Kanban kontent")

(defn save-kanban [parameters]
  "Saving Kanban")

(defn validate-key [parameters] true)

(def valid-actions {"put" :put "get" :get "key" :key})

(def actions {:get read-kanban
              :put save-kanban
              :key validate-key})

(defn mpk-handler [request]
  (let [{action "action"} (:params request)]
    (if (or (nil? action) (nil? (get valid-actions action)))
      (-> (response "Invalid method") (status 405))
      (response (str (get valid-actions action))))))

(def mpk-app (wrap-params mpk-handler))

(defn -main [& args]
  (do-build-configuration args)
  (run-jetty mpk-app @configuration))
