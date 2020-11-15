from code.base.base_sql import Sql
from code.sql.sql_query import *


class Provider:
    @classmethod
    def get_all_info(cls):
        all_info = []
        query = SQL_SELECT_COMPANY_INFO
        answer = Sql.exec(query=query)
        for que in answer:
            query = SQL_SELECT_INFO_COURIER_IN_COMPANY.format(id=que["id"])
            answers = Sql.exec(query=query)
            info_company = []
            for qu in answers:
                query = SQL_SELECT_ORDERS_COURIER.format(id=qu["id"])
                qu["orders"] = Sql.exec(query=query)
                info_company.append(qu)
            all_info.append({"name": que.get("name"), "requisites": que.get('name'), "data": info_company})
        return all_info

    @classmethod
    def get_gps_couriers(cls):
        query = SQL_SELECT_ALL_COURIERS
        return Sql.exec(query=query)

    @classmethod
    def get_gps_courier_id(cls, courier_id):
        query = SQL_SELECT_INFO_COURIER.format(id=courier_id)
        return Sql.exec(query=query)

    @classmethod
    def get_active_orders(cls):
        query = SQL_SELECT_ACTIVE_ORDERS
        return Sql.exec(query=query)


