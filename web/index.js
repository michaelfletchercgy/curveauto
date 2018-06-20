Vue.component('car-panel', {
    props: ['car'],
    data: function () {
        return {
            editing: false,
            carTypes: null,
            maintenanceTypes: null,
            carType: null,
            vin: null,
            make: null,
            model: null,
            year: null,
            odometer: null,
            carMaintenance:null,
            maintenanceType: null,
            carTypeId: null
        };
    },
    created: function () {
        this.fetchCarTypes();
        this.fetchMaintenanceTypes();
        this.fetchCarMaintenance();
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

            if (!this.carTypeId) {
                alert("Please select a car type.");
            } else {
                var post = {
                    id: this.car.id,
                    carType: {
                        id: this.carTypeId
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

        fetchMaintenanceTypes: function() {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('GET', 'api/maintenanceTypes');
            xhr.onload = function () {
                self.maintenanceTypes = JSON.parse(xhr.responseText);
            }
            xhr.send();
        },

        fetchCarMaintenance: function() {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('GET', 'api/cars/' + this.car.id + '/maintenance');
            xhr.onload = function () {
                self.carMaintenance = JSON.parse(xhr.responseText);
            }
            xhr.send();
        },

        scheduleMaintenance: function(x) {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('PUT', 'api/carMaintenance');
            xhr.setRequestHeader('Content-type','application/json');
            xhr.onload = function () {
                var result = JSON.parse(xhr.responseText);

                if (result.error) {
                    alert(result.error);
                } else {
                    self.fetchCarMaintenance();
                }
            };


            if (!this.maintenanceType) {
                alert('Please select a maintenance type.');
                return;
            }

            var post = {
                car: {
                    id: this.car.id
                },
                maintenanceType: {
                    id: this.maintenanceType
                }
            };

            xhr.send(JSON.stringify(post));
        },

        deleteMaintenance: function(cm) {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('DELETE', 'api/carMaintenance/' + cm.id);
            xhr.onload = function () {
                self.fetchCarMaintenance();
            }
            xhr.send();
        }
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