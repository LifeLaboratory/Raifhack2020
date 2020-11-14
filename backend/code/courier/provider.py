from code import api
from flask_restplus import Resource, Api
from code.base.base_sql import Sql
from code.sql.sql_query import *


class Provider:

    @classmethod
    def get_courier_orders(cls, courier_id):
        query = SQL_SELECT_ORDERS_COURIER.format(id=courier_id)
        return Sql.exec(query=query)
