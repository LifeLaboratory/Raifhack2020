
const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/Index.vue')},
      { path: 'courier_list', component: () => import('pages/Couriers.vue')},
      { path: 'couer/detail_map/:id', props: true, component: () => import('pages/Detail_map.vue')},
      { path: 'company/', component: () => import('pages/Company.vue')},
      { path: 'orders/', component: () => import('pages/Orders.vue')},

    ]
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '*',
    component: () => import('pages/Error404.vue')
  }
]

export default routes
