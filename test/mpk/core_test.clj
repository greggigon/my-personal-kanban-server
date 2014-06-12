(ns mpk.core-test
  (:require [clojure.test :refer :all]
            [mpk.core :refer :all]
            [mpk.action-handlers :as handlers :refer :all]))

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

(deftest test-decorating-jsonp-callbacks
  (testing "If returned body of response is wrapped in JSONP callback"
    (is (= (:body (decorate-response-in-jsonp-callback {"callback" "callback"} (handlers/->Response 200 "{}" {}))) "callback({})"))))

(deftest test-rejecting-non-service-requests
  (testing "Should reject non service requests"
    (is (= (:status (mpk-handler {:uri "/some"})) 405))))
