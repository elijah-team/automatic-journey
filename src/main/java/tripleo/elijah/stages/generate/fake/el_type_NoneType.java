package tripleo.elijah.stages.generate.fake;

public class el_type_NoneType implements el_type {
    @Override
    public el_type_type type() {
        return el_type_type.NONE;
    }

    @Override
    public el_type_rider rider() {
        return new el_type_rider_NONE();
    }
}
