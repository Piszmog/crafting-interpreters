.PHONE: all
all: cmake build run
cmake:
	$(echo "Running cmake")
	@mkdir -p build && cd build && cmake ..
build:
	$(echo "Running make")
	@cd build && make
run:
	$(echo "Running clox")
	@cd build && ./clox
