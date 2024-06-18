package tripleo.elijah.comp;

class CSS2_AlmostComplete implements CSS2_Signal {
    @Override
    public void trigger(Compilation compilation, Object payload) {
        compilation._cis.almostComplete(compilation._cio);
    }
}
