# coding=utf-8

import requests as req
import json

from datetime import datetime, timedelta


def registration_qr(order, amount=10):
    """
    Формирует и направляет запрос на регистрацию QR-кода
    :param order: String, уникальный идентификатор заказа
    :param amount: Decimal, сумма в рублях.
    :return: dict, пример
                {
                    "code": "SUCCESS",
                    "qrId": "AD100004BAL7227F9BNP6KNE007J9B3K",
                    "payload": "https://qr.nspk.ru/AD100004BAL7227F9BNP6KNE007J9B3K?type=02&bank=100000000007&sum=1&cur=RUB&crc=AB75",
                    "qrUrl": "https://e-commerce.raiffeisen.ru/api/sbp/v1/qr/AD100004BAL7227F9BNP6KNE007J9B3K/image"
                }
    """
    paydt = datetime.now()
    life_date = paydt + timedelta(minutes=2)
    tz = "+07:00"
    endpoint = 'https://test.ecom.raiffeisen.ru/api/sbp/v1/qr/register'
    merchant_id = "MA473271"
    data = {
        # "account": ,
        "additionalInfo": "RifeHuck 2020 Team=LIFE Laboratory",
        "amount": amount,
        "createDate": paydt.isoformat() + tz,
        "currency": "RUB",
        "order": order,
        "paymentDetails": "Проверка работы СБП и от чистого сердца",
        "qrType": "QRDynamic",
        "qrExpirationDate": life_date.isoformat() + tz,
        "sbpMerchantId": merchant_id
    }

    headers = {"Content-Type": "application/json"}
    data_r = req.post(endpoint, data=json.dumps(data), headers=headers)

    return json.loads(data_r.text)
