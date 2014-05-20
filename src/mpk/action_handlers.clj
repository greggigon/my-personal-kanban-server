(ns mpk.action-handlers
  (:require [mpk.storage :refer :all]
            [mpk.configuration :refer :all]
            [clojure.data.json :as json]
            ))

(defprotocol ActionHandler
  (perform [this params session]))

(defrecord Response [status headers body])

(deftype SaveHandler []
  ActionHandler
  (perform [this params session]
          (Response. 200 {"Content-Type" "text/plain"} "Yuppi, save-handler works")))


(deftype ReadHandler []
  ActionHandler
  (perform [this params session]
          (Response. 200 {"Content-Type" "text/plain"} "YES, the read-handler is ON")))




(deftype KeyHandler []
  ActionHandler
  (perform [this params session]
           "Checks if the file named by Kanban Key is in the folder and returns it's timestamp. Key is always valid"
          (let [{kanban-key "kanbanKey"} params directory (:directory @configuration)]
            (Response.
           200
           {"Content-Type" "application/json"}
           (json/write-str {"success" true "lastUpdated" (last-updated directory kanban-key)})))))

