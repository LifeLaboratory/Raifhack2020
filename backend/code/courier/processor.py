from code import api
from flask_restplus import Resource, fields, reqparse
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
        parser = reqparse.RequestParser()
        parser.add_argument('lat', type=float, help='lat')
        parser.add_argument('lon', type=float, help='lon')
        args = parser.parse_args()
        return Provider.post_courier_gps(courier_id, args['lat'], args['lon'])


@api.route('/api/courier/<courier_id>')
class Client(Resource):

    def get(self, courier_id):
        return Provider.get_courier_info(courier_id)[0]


@api.route('/api/couriers')
class Clients(Resource):

    def get(self):
        return Provider.get_couriers_info()


