Vue.component('car-panel', {
    props: ['car'],
    data: function () {
        return {
            editing: false,
            carTypes: null,
            carType: null,
            vin: null,
            make: null,
            model: null,
            year: null,
            odometer: null
        };
    },
    created: function () {
        this.fetchCarTypes();
    },
    methods: {

        editCar: function() {
            this.editing = true;
            this.carTypeId = this.car.carType.id;
            this.vin = this.car.vin;
            this.make = this.car.make;
            this.model = this.car.model;
            this.year = this.car.year;
            this.odometer = this.car.odometer;
        },

        saveCar: function() {
            this.editing = false;

            var xhr = new XMLHttpRequest();
            var self = this;

            if (this.car.id == 0) {
                xhr.open('PUT', 'api/cars');
            } else {
                xhr.open('POST', 'api/cars/' + this.car.id);
            }

            xhr.setRequestHeader('Content-type','application/json');
            xhr.onload = function () {
                self.$emit('refresh');
            }
            var carTypeEl = document.getElementById("carType");

            if (carTypeEl.selectedOptions.length == 0) {
                alert("Please select a car type.");
            } else {
                var carType = document.getElementById("carType").selectedOptions[0].value;

                var post = {
                    id: this.car.id,
                    carType: {
                        id: parseInt(carType)
                    },
                    vin: this.vin,
                    make: this.make,
                    model: this.model,
                    year: this.year,
                    odometer: this.odometer
                };

                var json = JSON.stringify(post)
                xhr.send(json);
            }
        },

        deleteCar: function(car) {

            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('DELETE', 'api/cars/' + car.id);
            xhr.onload = function () {
                self.$emit('refresh');
            }
            xhr.send();
        },

        fetchCarTypes: function() {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('GET', 'api/carTypes');
            xhr.onload = function () {
                self.carTypes = JSON.parse(xhr.responseText);
            }
            xhr.send();
        },
    },
    template: '#car-panel-template'
});

var vm = new Vue({

    el: '#cars',

    data: {
        cars: null,
    },
    created: function () {
        this.fetchData();
    },

    methods: {
        fetchData: function () {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('GET', 'api/cars');
            xhr.onload = function () {
                self.cars = JSON.parse(xhr.responseText);
            }
            xhr.send();
        },
        newCar: function() {
            this.cars.push({ id: 0, carType: {}});
        }
    }
});