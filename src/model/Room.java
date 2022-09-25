package model;

public class Room implements IRoom {
    protected String roomNumber;
    protected Double price;
    protected RoomType roomType;

    public Room(String roomNumber, Double price, RoomType roomType){
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return price;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean isFree() {
        return price == 0;
    }

    @Override
    public String toString(){
        return "Room No." + roomNumber + ": $" + price + " per night"
                + " " + roomType + " room";
    }
}
