(ns mpk.core-test
  (:require [clojure.test :refer :all]
            [mpk.core :refer :all]))

(deftest test-valid-actions-only
    (testing "If only valid actions are accepted"
     (are [request _ is-valid] (= (valid-action? request) is-valid)
          {:params {"action" "put"}} => true
          {:params {"action" "get"}} => true
          {:params {"action" "key"}} => true
          {:params {"action" "unsupported"}} => false
          {:params {}} => false)))

(deftest test-callback-params-check
  (testing "If the callback is missing blow response with 405"
    (is (= (:status (check-callback-paramater {"foo" "bar"} {} '#(str %1 %2))) 405))))
