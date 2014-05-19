(ns mpk.core-test
  (:require [clojure.test :refer :all]
            [mpk.core :refer :all]))

(deftest test-valid-actions-only
    (testing "If only valid actions are accepted"
     (are [request _ status] (= (:status (mpk-handler request)) status)
          {:params {"action" "put"}} => 200
          {:params {"action" "get"}} => 200
          {:params {"action" "key"}} => 200
          {:params {"action" "unsupported"}} => 405
          {:params {}} => 405)))


