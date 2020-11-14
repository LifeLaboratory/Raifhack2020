SQL_SELECT_INFO_CLIENT = """
SELECT *
FROM raifhack.clients
WHERE id={id}
"""

SQL_SELECT_INFO_COURIER = """
SELECT *
FROM raifhack.couriers
WHERE id={id}
"""

SQL_SELECT_ORDERS_COURIER = """
SELECT *
FROM raifhack.orders
WHERE number_courier={id}
"""

SQL_SELECT_COMPANY_INFO = """
SELECT *
FROM raifhack.companies
"""

SQL_SELECT_INFO_COURIER_IN_COMPANY = """
SELECT *
FROM raifhack.couriers
WHERE num_company={id}
"""

SQL_SELECT_ALL_COURIERS = """
SELECT *
FROM raifhack.couriers
"""
