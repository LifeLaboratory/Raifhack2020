import requests as req

"""
API SBP: https://e-commerce.raiffeisen.ru/api/doc/sbp.html#tag/qr-controller
"""

class SBPClient:
    def __init__(self):
        self.session = req.Session()
        self.base_host = 'https://test.ecom.raiffeisen.ru'

    def __del__(self):
        self.session.close()

    def _get_url(self, endpoint):
        return self.base_host + endpoint

    def _make_request(self, method='GET', endpoint='', headers=None, **kwargs):
        self.session.headers.update({'Content-Type': 'application/json;charset=UTF-8'})

        if headers:
            self.session.headers.update(headers)

        kwargs.update({'verify': False})

        if method == 'GET':
            self.session.get(self._get_url(endpoint), **kwargs)
        elif method == 'POST':
            self.session.post(self._get_url(endpoint), **kwargs)

    def register_qr_code(self, createDate, order, qrTypes, sbpMerchantId, **kwargs):
        """
        :account - string <= 20 символов
        :additionalInfo - string <= 140 символов
        :amount - BigDecimal
        :currency - string <= 3 символа
        :paymentDetails - string - <= 185 символов
        :qrExpirationDate - string <YYYY-MM-DD ТHH24:MM:SS±HH:MM>

        @required
        :createDate - string <YYYY-MM-DD ТHH24:MM:SS±HH:MM>
        :order - string < 40 символов
        :qrType - string <QRStatic/QRDynamic>
        :sbpMerchantId - string <= 12 characters
        """

        endpoint = '/api/sbp/v1/qr/register'

        kwargs.update({
            'createDate': createDate,
            'order': order,
            'qrTypes': qrTypes,
            'sbpMerchantId': sbpMerchantId
        })

        self._make_request(method='POST', endpoint=endpoint, **kwargs)

    def get_data_from_registered_qr(self, qrId, secret_key):
        endpoint = f'/api/sbp/v1/qr/{qrId}/payment-info'
        kwargs = {'qrId': qrId}
        headers = {'Authorization': f'Bearer {secret_key}'}
        self._make_request(method='GET', endpoint=endpoint, headers=headers, **kwargs)

    def get_info_by_payment(self, qrId, secret_key):
        endpoint = f'/api/sbp/v1/qr/{qrId}/info'
        kwargs = {'qrId': qrId}
        headers = {'Authorization': f'Bearer {secret_key}'}
        self._make_request(method='GET', endpoint=endpoint, headers=headers, **kwargs)

    def payment_refund(self, secret_key, amount, order, refundId, **kwargs):
        """
        :paymentDetails - string <= 180 символов
        :transactionId - long

        @required:
        :order - string <= 40 символов
        :refundId - string <= 255 символов
        :amount - BigDecimal
        """

        endpoint = '/api/sbp/v1/refund'
        kwargs.update({
            'amount': amount,
            'order': order,
            'refundId': refundId,
        })
        headers = {'Authorization': f'Bearer {secret_key}'}
        self._make_request(method='POST', endpoint=endpoint, headers=headers, **kwargs)

    def get_info_by_refund(self, refundId, secret_key):
        endpoint = f'/api/sbp/v1/qr/{refundId}/info'
        kwargs = {'refundId': refundId}
        headers = {'Authorization': f'Bearer {secret_key}'}
        self._make_request(method='GET', endpoint=endpoint, headers=headers, **kwargs)

if __name__ == '__main__':





