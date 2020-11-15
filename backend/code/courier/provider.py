from code import api
from flask_restplus import Resource, Api
from code.base.base_sql import Sql
from code.sql.sql_query import *


class Provider:

    @classmethod
    def get_courier_orders(cls, courier_id):
        query = SQL_SELECT_ORDERS_COURIER.format(id=courier_id)
        return Sql.exec(query=query)

    @classmethod
    def post_courier_gps(cls, courier_id, lat, lon):
        query = SQL_UPDATE_COURIER_GPS.format(id=courier_id, lat=lat, lon=lon)
        try:
            answer = Sql.exec(query=query)[0]["bool"]
        except:
            answer = False
        return answer

    @classmethod
    def get_courier_info(cls, courier_id):
        query = SQL_SELECT_INFO_COURIER.format(id=courier_id)
        return Sql.exec(query=query)

    @classmethod
    def get_couriers_info(cls):
        query = SQL_SELECT_INFO_COURIERS
        return Sql.exec(query=query)