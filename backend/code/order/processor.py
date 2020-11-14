from code import api
from flask_restplus import Resource, fields
from code.order.provider import Provider

a_test = api.model('Order', {'msg': fields.String('PONG!')})
a_test2 = api.model('OrderId', {'data': fields.String('test_data')})


@api.route('/order')
class Order(Resource):

    def get(self):
        return Provider.get_all_orders()


@api.route('/order/<order_id>')
class OrderId(Resource):

    def get(self, order_id):
        return Provider.get_order(order_id)
