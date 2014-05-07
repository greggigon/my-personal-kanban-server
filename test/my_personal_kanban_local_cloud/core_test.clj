(ns my-personal-kanban-local-cloud.core-test
  (:require [clojure.test :refer :all]
            [my-personal-kanban-local-cloud.core :refer :all]))

(deftest test-validating-key
  (testing "Key validation, should always report as key valid for any key"
    (is (= (validate-key "sdrt567ujhnb nmjhg") true))))

(deftest test-valid-actions-only
    (testing "If only valid actions are accepted"
     (are [request _ status] (= (:status (mpk-handler request)) status)
          {:params {"action" "put"}} => 200
          {:params {"action" "get"}} => 200
          {:params {"action" "key"}} => 200
          {:params {"action" "unsupported"}} => 405
          {:params {}} => 405)))


