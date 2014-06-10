(ns mpk.action-handlers
  (:require [mpk.storage :refer :all]
            [mpk.configuration :refer :all]
            [clojure.data.json :as json]
            [clojure.string :refer [join]]
            )
  (:import [java.io IOException]))

(defprotocol ActionHandler
  (perform [this params session]))

(defrecord Response [status body session])

(defn handle-error-within-body [message]
  (Response. 200
             (json/write-str {"success" false, "error" message})
             {}))

(defn kanban-with-key-was-persisted? [directory kanban-key]
  (not= (last-updated directory kanban-key) 0))

(defn create-empty-array [size]
  (vec (repeat size nil)))

(defn- handle-fragments
  [fragments]
  (Response. 200 "" {:number-of-fragments fragments :fragments (create-empty-array fragments)}))

(defn- handle-chunks
  [chunk-number czunk session]
  (if-let [fragments-in-session (:fragments session)]
    (Response. 200 "" (assoc session :fragments (assoc fragments-in-session (- chunk-number 1) czunk)))
    (handle-error-within-body "Some old junk in the session. Try to save again.")))

(defn- handle-hash [hasz kanban-key session]
  (let [full-kanban (join (:fragments session)) calc-hash (md5 full-kanban)]
    (if (= hasz calc-hash)
      (try
        (do
          (save (:directory @configuration) kanban-key full-kanban)
          (Response. 201 (json/write-str {"success" true}) {}))
        (catch IOException ex
          (handle-error-within-body (str "Unable to save Kanban - " (.getMessage ex)))))
      (handle-error-within-body (str "Received kanban doesn't validate with Hash. " hasz " <=> " calc-hash)))))

(deftype SaveHandler []
  ActionHandler
  (perform [this params session]
           (let [fragments (get params "fragments")
                 chunk-number (get params "chunkNumber")
                 czunk (get params "chunk")
                 hasz (get params "hash")
                 kanban-key (get params "kanbanKey")]
             (cond
              (not (nil? fragments)) (handle-fragments (int (read-string fragments)))
              (not (nil? chunk-number)) (handle-chunks (int (read-string chunk-number)) czunk session)
              (not (nil? hasz)) (handle-hash hasz kanban-key session)
             ))))


(deftype ReadHandler []
  ActionHandler
  (perform [this params session]
    (let [{kanban-key "kanbanKey"} params directory (:directory @configuration)]
      (if (kanban-with-key-was-persisted? directory kanban-key)
        (Response. 200
                   (json/write-str {"success" true
                                    "lastUpdated" (last-updated directory kanban-key)
                                    "kanban" (load-kanban directory kanban-key)})
                   {})
        (handle-error-within-body (str "Kanban with key [" kanban-key "] was never persisted"))))))




(deftype KeyHandler []
  ActionHandler
  (perform [this params session]
    "Checks if the file named by Kanban Key is in the folder and returns it's timestamp. Key is always valid"
    (let [{kanban-key "kanbanKey"} params directory (:directory @configuration)]
      (Response. 200
                 (json/write-str {"success" true
                                  "lastUpdated" (last-updated directory kanban-key)})
                 {}))))

