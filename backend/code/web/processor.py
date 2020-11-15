from code import api
from flask_restplus import Resource
from code.web.provider import Provider


@api.route('/api/web/all_info')
class AllInfo(Resource):

    def get(self):
        return Provider.get_all_info()


@api.route('/api/web/gps_couriers')
class GpsCouriers(Resource):

    def get(self):
        return Provider.get_gps_couriers()


@api.route('/api/web/gps_courier/<courier_id>')
class GpsCourierId(Resource):

    def get(self, courier_id):
        return Provider.get_gps_courier_id(courier_id)[0]


@api.route('/api/web/active_orders')
class ActiveOrders(Resource):

    def get(self):
        return Provider.get_active_orders()