(ns mpk.storage-test
  (:require [clojure.test :refer :all]
            [mpk.storage :refer :all])
  (:import (java.io File)))


(def temporary-kanban-folder (str (System/getProperty "java.io.tmpdir") "/mpk"))


(deftest test-persistence-of-content
  (testing "Testing if persistence to disk happens"
    (.mkdir (File. temporary-kanban-folder))
    (let [file (str temporary-kanban-folder "/blabla-key" file-extension)]
      (save temporary-kanban-folder "blabla-key" "kanban content")
      (is (= (slurp file) "kanban content"))
      (.delete (File. temporary-kanban-folder file)))
    (.delete (File. temporary-kanban-folder))))


(deftest test-persistence-of-content-and-overwrite
  (testing "Testing if persisting file for the second time overrides content"
    (.mkdir (File. temporary-kanban-folder))
    (let [file (str temporary-kanban-folder "/blabla-key" file-extension)]
      (save temporary-kanban-folder "blabla-key" "kanban content")
      (save temporary-kanban-folder "blabla-key" "kanban content 2")
      (is (= (slurp file) "kanban content 2")))
    (.delete (File. temporary-kanban-folder "blabla-key.data"))
    (.delete (File. temporary-kanban-folder))))


(deftest test-should-load-content-from-disk
  (testing "Testing if the thing loads Kanban content from disk"
    (.mkdir (File. temporary-kanban-folder))
    (let [file (str temporary-kanban-folder "/foobar" file-extension)]
      (spit file "{\"some\":\"content\"}")
      (is (= (load-kanban temporary-kanban-folder "foobar") "{\"some\":\"content\"}"))
      (.delete (File. file)))
    (.delete (File. temporary-kanban-folder))))


(deftest test-creation-of-md5-hashes
  (testing "If the valid MD5 hash was created"
    (are [to-hash _ result] (= (md5 to-hash) result)
         "a"       => "0cc175b9c0f1b6a831c399e269772661"
         "foo bar" => "327b6f07435811239bc47e1544353273")))

(deftest test-timestamp-on-file
  (testing "Timesamp of last update on file returned by storage funcitons"
    (.mkdir (File. temporary-kanban-folder))
    (save temporary-kanban-folder "foobar" "content")
    (is (not (= (last-updated temporary-kanban-folder "foobar") 0)))
    (.delete (File. temporary-kanban-folder "foobar.data"))
    (.delete (File. temporary-kanban-folder))))

(deftest test-timestamp-on-non-existing-file
  (testing "Timesamp of last update on key that doesn't extist should be 0"
    (is (= (last-updated temporary-kanban-folder "foobar") 0))))

