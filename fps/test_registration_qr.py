# coding=utf-8

import unittest
import requests as req
import json

from datetime import datetime, timedelta


class TestRegistrationQr(unittest.TestCase):
    def test_registration_qr(self):

        paydt = datetime.now()
        life_date = paydt + timedelta(minutes=2)
        tz = "+07:00"
        endpoint = 'https://test.ecom.raiffeisen.ru/api/sbp/v1/qr/register'
        data = {
            # "account": ,
            "additionalInfo": "RifeHuck 2020",
            "amount": 10,
            # "createDate": "2020-11-14T23:14:38.107227+03:00",
            "createDate": paydt.isoformat()+tz,
            "currency": "RUB",
            "order": "1-22-333",
            "paymentDetails": "Проверка работы СБП и от чистого сердца",
            "qrType": "QRDynamic",
            # "qrExpirationDate": "2020-11-15T00:59:38.107227+03:00",
            "qrExpirationDate": life_date.isoformat()+tz,
            "sbpMerchantId": "MA473271"
        }

        headers = {"Content-Type": "application/json"}

        data_r = req.post(endpoint, data=json.dumps(data), headers=headers)
        print(data_r.text)
        self.assertEqual(data_r.status_code, 200)
        self.assertIsNotNone(data_r.text)
        return

    def test_payment_info(self):

        endpoint_payment_info = 'https://test.ecom.raiffeisen.ru/api/sbp/v1/qr/{qrId}/payment-info -H {SecretKey}'
        secret_key = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNQTQ3MzI3MSIsImp0aSI6IjIwOWI3MjNkLTdmZmItNDZhZi05YzU4LWFmZjFiMWI4YzRlNSJ9.lLZtvflKgxHPTmaZbH4cnOw2_1NE_f4LFP7fbVVnOSc'
        # secret_key = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMDAwMDMzMzMzMjgwMTctMzMzMjgwMTciLCJqdGkiOiIyNWE5NTllMi1mNmY1LTQwNWItYjA5Ni01MjA4NjJhODViNjAifQ.8CiuZp6t-DBstOCCno5OnV3tAUTEnh5OI6wLyBMesU0'
        endpoint_register = 'https://test.ecom.raiffeisen.ru/api/sbp/v1/qr/register'

        paydt = datetime.now()
        life_date = paydt + timedelta(minutes=2)
        tz = "+07:00"
        data = {
            # "account": ,
            "additionalInfo": "RifeHuck 2020",
            "amount": 10,
            "createDate": paydt.isoformat()+tz,
            "currency": "RUB",
            "order": "1-22-333",
            "paymentDetails": "Проверка работы СБП и от чистого сердца",
            "qrType": "QRDynamic",
            "qrExpirationDate": life_date.isoformat()+tz,
            "sbpMerchantId": "MA473271"
        }

        headers = {"Content-Type": "application/json"}

        data_r = req.post(endpoint_register, data=json.dumps(data), headers=headers)
        print(data_r.text)

        data_for_check = endpoint_payment_info.format(qrId=json.loads(data_r.text)["qrId"], SecretKey=secret_key)

        data_rc = req.get(data_for_check)
        print(data_rc.text)
        self.assertEqual(data_r.status_code, 200)
        self.assertIsNotNone(data_r.text)

        import time
        for _ in range(0,10):
            time.sleep(2)
            data_rc = req.get(data_for_check)
            print(data_rc.text)

if __name__ == '__main__':
    unittest.main()