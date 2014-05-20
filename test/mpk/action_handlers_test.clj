(ns mpk.action-handlers-test
  (:require [mpk.configuration :refer :all]
            [mpk.action-handlers :as handlers :refer :all]
            [clojure.test :refer :all]
            [mpk.file-utils :as file-utils :refer :all])
  (:import (java.io File)))

(def temporary-kanban-folder (str (System/getProperty "java.io.tmpdir") "/mpk"))


(deftest test-the-key-validation-method-no-file
  (testing "Should always return true for key validation"
    (do-build-configuration ["directory" temporary-kanban-folder])
    (file-utils/make-folder temporary-kanban-folder)
    (let [key-handler (handlers/->KeyHandler)]
      (is (= (perform key-handler {"kanbanKey" "foobar"} {})
             (handlers/->Response 200 {"Content-Type" "application/json"} "{\"success\":true,\"lastUpdated\":0}"))))
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