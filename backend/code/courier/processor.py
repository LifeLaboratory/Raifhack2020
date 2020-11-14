from code import api
from flask_restplus import Resource, fields
from code.courier.provider import Provider

a_test = api.model('Test', {'msg': fields.String('PONG!')})
a_test2 = api.model('Test2', {'data': fields.String('test_data')})


@api.route('/api/courier/orders/<courier_id>')
class CourierOrders(Resource):

    def get(self, courier_id):
        return Provider.get_courier_orders(courier_id)


@api.route('/api/courier/orders/<courier_id>')
class CourierGps(Resource):

    def post(self, courier_id):
        if self.request.method == 'POST':
            return Provider.post_courier_gps(courier_id, self.request.form['lat'], self.request.form['lon'])

