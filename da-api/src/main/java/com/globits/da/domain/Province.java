package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_province")
public class Province extends BaseObject {
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province",
            cascade = CascadeType.ALL)
    private List<District> districtList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province",
            cascade = CascadeType.ALL)
    private List<Diploma> diplomaList = new ArrayList<>();
}
