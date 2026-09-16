package dev.kacperm.opmulti.multi;

import java.util.List;

public record MultiData(List<MultiSource> permanent, List<MultiSource> temporary) {

    public double permanentTotal() {
        return this.permanent.stream().mapToDouble(MultiSource::value).sum();
    }

    public double temporaryTotal() {
        return this.temporary.stream().mapToDouble(MultiSource::value).sum();
    }

    public double total() {
        return this.permanentTotal() + this.temporaryTotal();
    }
}
