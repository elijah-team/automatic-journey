package tripleo.elijah.stages.generate.fake;

public interface el_type {
    el_type_type type();

    el_type_rider rider();

    enum el_type_type {
        NONE, CLASS // none(t), class(k)
    }

    interface el_type_rider {
    }

    class el_type_rider_NONE implements el_type_rider {
        el_type t;
    }
}
