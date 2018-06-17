var cars = new Vue({

  el: '#cars',

  data: {
    cars: null
  },

  created: function () {
    this.fetchData()
  },

  methods: {
    fetchData: function () {
    console.log('ftching');
      var xhr = new XMLHttpRequest();
      var self = this;
      xhr.open('GET', 'api/cars');
      xhr.onload = function () {
        self.cars = JSON.parse(xhr.responseText);
        console.log("loaded ", self.cars);
      }
      xhr.send();
    }
  }
})