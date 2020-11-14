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

SQL_SELECT_ACTIVE_ORDERS = """
SELECT *
FROM raifhack.orders AS ord
LEFT JOIN raifhack.couriers AS co ON (co.id = ord.number_courier)
LEFT JOIN raifhack.clients AS cl ON (cl.id = ord.number_client)
WHERE ord.status_order is NULL
"""
