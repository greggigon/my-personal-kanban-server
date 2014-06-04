(ns mpk.action-handlers-test
  (:require [mpk.configuration :refer :all]
            [mpk.action-handlers :as handlers :refer :all]
            [clojure.test :refer :all]
            [mpk.file-utils :as file-utils :refer :all]
            [clojure.data.json :as json])
  (:import (java.io File)))

(def temporary-kanban-folder (str (System/getProperty "java.io.tmpdir") "/mpk"))


(deftest test-the-key-validation-method-no-file
  (testing "Should always return true for key validation"
    (do-build-configuration ["directory" temporary-kanban-folder])
    (file-utils/make-folder temporary-kanban-folder)
    (let [key-handler (handlers/->KeyHandler)]
      (is (= (perform key-handler {"kanbanKey" "foobar"} {})
             (handlers/->Response 200 {"Content-Type" "application/json"} "{\"success\":true,\"lastUpdated\":0}" {}))))
    (file-utils/clean-folder temporary-kanban-folder)))

(deftest test-the-key-validation-method-with-file
  (testing "Should return value of last modified file"
    (do-build-configuration ["directory" temporary-kanban-folder])
    (file-utils/make-folder temporary-kanban-folder)
    (let [temp-file (File. temporary-kanban-folder "gregster.data")]
      (spit (.getAbsolutePath temp-file) (pr-str "dupa blada"))
    (let [key-handler (handlers/->KeyHandler)]
      (let [response (perform key-handler {"kanbanKey" "gregster"} {})]
        (is (not (= (:body response)
                  "{\"success\":true,\"lastUpdated\":0}"))))))
    (file-utils/clean-folder temporary-kanban-folder)))

(deftest test-reading-persistent-kanban
  (testing "Should read Kanban previously persisted and return content in the response body"
    (do-build-configuration ["directory" temporary-kanban-folder])
    (file-utils/make-folder temporary-kanban-folder)
    (let [temp-file (File. temporary-kanban-folder "gregster.data")]
      (spit (.getAbsolutePath temp-file) "Kanban content")
      (let [read-handler (handlers/->ReadHandler) last-updated (.lastModified temp-file)]
        (is (=
             (:body (perform read-handler {"kanbanKey" "gregster"} {}))
             (json/write-str {"success" true, "lastUpdated" last-updated, "kanban" "Kanban content"})))))
    (file-utils/clean-folder temporary-kanban-folder)))

(deftest test-handling-error-within-body
  (testing "Handling errors within body"
    (is (=
         (handle-error-within-body "Error message")
         (handlers/->Response 200
                    {"Content-Type" "application/json"}
                    (json/write-str {"success" false, "error" "Error message"})
                    {})))))

(deftest test-reading-non-existing-kanban
  (testing "Should return error within body if Kanban doesn't exist"
    (let [read-handler (handlers/->ReadHandler)
          response (perform read-handler {"kanbanKey" "foobarboo"} {})]
      (is (= (:body response) (json/write-str {"success" false "error" "Kanban with key [foobarboo] was never persisted"})))
      (is (= (:status response) 200)))))

(deftest test-processing-save-session-starts
  (testing "Should start session when fragments in parameters list"
    (let [save-handler (handlers/->SaveHandler)
          response (perform save-handler {"fragments" 12} {})]
      (is (= (:session response) {:number-of-fragments 12 :fragments []}))
      (is (= (:status response) 200)))))
