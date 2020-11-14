from code import api
from flask_restplus import Resource, fields
from code.order.provider import Provider

a_test = api.model('Test', {'msg': fields.String('PONG!')})
a_test2 = api.model('Test2', {'data': fields.String('test_data')})


@api.route('/api/order')
class Test(Resource):

    def get(self):
        return Provider.get_all_orders()


@api.route('/api/order/<integer:order_id>')
class Test2(Resource):

    def get(self, order_id):
        return Provider.get_order(order_id)
