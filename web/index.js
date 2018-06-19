var cars = new Vue({

    el: '#cars',

    data: {
        cars: null,
        carTypes: null,

        carType: null,
        carVin: null,
        carMake: null,
        carModel: null,
        carYear: null,
        carOdometer: null
    },

    created: function () {
        this.fetchData();
        this.fetchCarTypes();
    },

    methods: {
        fetchCarTypes: function() {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('GET', 'api/carTypes');
            xhr.onload = function () {
                self.carTypes = JSON.parse(xhr.responseText);
            }
            xhr.send();
        },

        fetchData: function () {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('GET', 'api/cars');
            xhr.onload = function () {
                self.cars = JSON.parse(xhr.responseText);
            }
            xhr.send();
        },

        deleteCar: function(car) {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('DELETE', 'api/cars/' + car.id);
            xhr.onload = function () {
                self.fetchData();
            }
            xhr.send();
        },

        saveNewCar: function() {
            console.log("saving new car");
        },

        save: function() {
            var xhr = new XMLHttpRequest();
            var self = this;
            xhr.open('PUT', 'api/cars');
            xhr.setRequestHeader('Content-type','application/json');
            xhr.onload = function () {
                this.carVin = null;
                this.carMake = null;
                this.carModel = null;
                this.carYear = null;
                this.carOdometer = null;

                self.fetchData();
            }
            var carTypeEl = document.getElementById("carType");

            if (carTypeEl.selectedOptions.length == 0) {
                alert("Please select a car type.");
            } else {
                var carType = document.getElementById("carType").selectedOptions[0].value;

                var post = {
                    id: 0,
                    carType: {
                        id: parseInt(carType)
                    },
                    vin: this.carVin,
                    make: this.carMake,
                    model: this.carModel,
                    year: this.carYear,
                    odometer: this.carOdometer
                };

                var json = JSON.stringify(post)
                xhr.send(json);
            }

        }
    }
})