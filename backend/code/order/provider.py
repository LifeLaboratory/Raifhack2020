from code import api
from flask_restplus import Resource, Api
from code.base.base_sql import Sql
from code.sql.sql_query import *


class Provider:
    @classmethod
    def create_order(cls, args):
        query = SQL_INSERT_ORDER_CREATE.format(**args)
        return Sql.exec(query=query)

    @classmethod
    def status_order(cls, id_order):
        query = SQL_SELECT_ORDER_STATUS.format(id=id_order)
        answer = Sql.exec(query=query)