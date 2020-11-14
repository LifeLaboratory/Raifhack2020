from code import api
from flask_restplus import Resource, fields, reqparse
from code.order.provider import Provider
from code.utils import sbp_client
from datetime import datetime

a_test = api.model('Test', {'msg': fields.String('PONG!')})
a_test2 = api.model('Test2', {'data': fields.String('test_data')})


@api.route('/api/order/create')
class Order(Resource):

    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('cost', type=float, help='cost')
        parser.add_argument('number_client', type=int, help='number_client')
        parser.add_argument('address', type=str, help='address')
        args = parser.parse_args()
        arguments = {}
        arguments["cost"] = args["cost"]
        arguments["number_client"] = args["number_client"]
        arguments["address"] = args["address"]
        arguments["number_courier"] = args["number_courier"]
        client = sbp_client.SBPClient()
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
            amount=args["cost"],
            additionalInfo='RifeHuck 2020',
            currency='RUB',
        )
        arguments["qr_code"] = registered.get('qrUrl')
        arguments["url_payload"] = registered.get('payload')
        arguments["qr_id"] = registered.get('qrId')
        return Provider.create_order(arguments)[0]["id"]


@api.route('/api/order')
class Order(Resource):

    def get(self):
        return 200
        #return Provider.get_all_orders()


@api.route('/api/order/<order_id>')
class OrderId(Resource):

    def get(self, order_id):
        return 200
        #return Provider.get_order(order_id)
