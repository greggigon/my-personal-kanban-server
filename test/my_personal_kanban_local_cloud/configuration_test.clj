(ns my-personal-kanban-local-cloud.configuration-test
  (:require [clojure.test :refer :all]
            [my-personal-kanban-local-cloud.configuration :refer :all]))

(deftest test-string-of-integer
  (testing "Testing returning integer when it's possible"
    (are [to-parse _ result] (= (number-if-one to-parse) result)
         "12"     => 12
         "twelve" => "twelve")))

(deftest test-turning-vectors
  (testing "Testing turning vectors of pairs into map"
    (is (= (vectors-to-map [["one" "two"]["three" "four"]] {}) {:one "two" :three "four"}))))


(deftest test-default-set-of-configuration
  (testing "Testing default set of configuration map when the passed list of parameters is empty"
    (do-build-configuration [])
    (is (= @configuration {:port 8080 :directory (str (System/getProperty "user.home") "/" "mpk")}))))

(deftest test-creating-configuration-elements
  (testing "Should setup and override all default configuration parameters"
    (do-build-configuration ["port" "8081" "directory" "/Tmp"])
    (is (= @configuration {:port 8081 :directory "/Tmp"}))))
