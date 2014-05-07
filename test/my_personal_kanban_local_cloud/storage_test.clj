(ns my-personal-kanban-local-cloud.storage-test
  (:require [clojure.test :refer :all]
            [my-personal-kanban-local-cloud.storage :refer :all])
  (:import (java.io File)))


(def temporary-kanban-folder (str (System/getProperty "java.io.tmpdir") "/mpk"))


(deftest test-persistence-of-content
  (testing "Testing if persistence to disk happens"
    (.mkdir (File. temporary-kanban-folder))
    (let [file (str temporary-kanban-folder "/blabla-key" file-extension)]
      (save temporary-kanban-folder "blabla-key" "kanban content")
      (is (= (read-string (slurp file)) "kanban content"))
      (.delete (File. temporary-kanban-folder file)))
    (.delete (File. temporary-kanban-folder))))


(deftest test-persistence-of-content-and-overwrite
  (testing "Testing if persisting file for the second time overrides content"
    (.mkdir (File. temporary-kanban-folder))
    (let [file (str temporary-kanban-folder "/blabla-key" file-extension)]
      (save temporary-kanban-folder "blabla-key" "kanban content")
      (save temporary-kanban-folder "blabla-key" "kanban content 2")
      (is (= (read-string (slurp file)) "kanban content 2")))
    (.delete (File. temporary-kanban-folder "blabla-key.data"))
    (.delete (File. temporary-kanban-folder))))


(deftest test-should-load-content-from-disk
  (testing "Testing if the thing loads Kanban content from disk"
    (.mkdir (File. temporary-kanban-folder))
    (let [file (str temporary-kanban-folder "/foobar" file-extension)]
      (spit file (pr-str "{\"some\":\"content\"}"))
      (is (= (load-kanban temporary-kanban-folder "foobar") "{\"some\":\"content\"}"))
      (.delete (File. file)))
    (.delete (File. temporary-kanban-folder))))
