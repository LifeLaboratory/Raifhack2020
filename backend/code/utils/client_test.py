from code.utils.sbp_client import SPBClient

if __name__ == '__main__':
    client = SBPClient()
    secret = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNQTQ3MzI3MSIsImp0aSI6IjIwOWI3MjNkLTdmZm' \
             'ItNDZhZi05YzU4LWFmZjFiMWI4YzRlNSJ9.lLZtvflKgxHPTmaZbH4cnOw2_1NE_f4LFP7fbVVnOSc'
    client.add_secret_key(secret)
    client.add_merchant_id('MA473271')

    registered = client.register_qr_code(
        createDate=datetime.now().isoformat() + '+07:00',
        order='1-22-33',
        qrTypes='QRDynamic',
        sbpMerchantId='MA473271',
        paymentDetails='Проверка работы',
        amount=10,
        additionalInfo='RifeHuck 2020',
        currency='RUB',
    )

    # print(registered)
    qrid = registered.get('qrId')

    payment = client.get_transaction(qrid)
    payment = client.get_qr_info(qrid)
